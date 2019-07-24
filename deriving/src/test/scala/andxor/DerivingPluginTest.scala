package andxor
package test

import andxor.argonaut._
import andxor.circe._
import andxor.scalacheck._
import andxor.types.ADTValue
import _root_.argonaut.{DecodeJson, EncodeJson}
import io.circe.{Decoder, Encoder}
import org.scalacheck.{Arbitrary, Properties}
import org.scalacheck.Prop.{BooleanOperators, forAllNoShrink}
import scalaz.{~>, Apply, Cord, Equal, Show}
import scalaz.Id.Id
import scalaz.Isomorphism.IsoFunctor
import scalaz.std.anyVal._
import scalaz.std.list._
import scalaz.std.option._
import scalaz.std.string._
import scalaz.syntax.apply._
import scalaz.syntax.equal._
import scalaz.syntax.std.string._

object typeclasses {
  implicit def showLabelled[A: Show, L <: Singleton with String]: Show[Labelled.Aux[A, L]] =
    Show.shows(l => s"""${l.label} := ${Show[A].shows(l.value)}""" ++ "\n")

  implicit def showAdtVal[A <: Singleton, L <: Singleton with String]: Show[Labelled.Aux[ADTValue[A], L]] =
    Show.shows(a => s"""ADTValue := ${a.label}""" ++ "\n")

  type ShowF[A] = A => Cord
  implicit val showIso: IsoFunctor[Show, ShowF] =
    IsoFunctor[Show, ShowF](Lambda[Show ~> ShowF](_.show _), Lambda[ShowF ~> Show](Show.show(_)))

  implicit val showDecide: Decidable[Show] = Decidable.fromIso[Show, ShowF](showIso)
  implicit val showDivide: Divide[Show] = Divide.fromIso[Show, ShowF](showIso)

  trait Read[A] { def read(s: String): Option[A] }
  object Read {
    implicit def readLabelled[A, L <: Singleton with String](implicit l: L, r: Read[A]): Read[Labelled.Aux[A, L]] =
      new Read[Labelled.Aux[A, L]] {
        def read(s: String): Option[Labelled.Aux[A, L]] =
          s.split("\n").toList.flatMap(_.split(s"$l := ", 2).lift(1).flatMap(r.read(_))).headOption.map(Labelled(_, l))
      }

    implicit def readAdtVal[A <: Singleton, L <: Singleton with String](
      implicit
      label: L,
      value: Labelled.Aux[ADTValue[A], L]): Read[Labelled.Aux[ADTValue[A], L]] =
      new Read[Labelled.Aux[ADTValue[A], L]] {
        def read(s: String): Option[Labelled.Aux[ADTValue[A], L]] =
          s.split("\n").toList.flatMap(_.split(s"ADTValue := ", 2).lift(1).filter(_ == label)).headOption.map(_ => value)
      }

    implicit def readList[A: Read]: Read[List[A]] = new Read[List[A]] {
      def read(s: String): Option[List[A]] = Some(Nil)
    }

    implicit def readOption[A](implicit r: Read[A]): Read[Option[A]] = new Read[Option[A]] {
      def read(s: String): Option[Option[A]] = r.read(s).map(Some(_))
    }

    implicit val readStr: Read[String] = new Read[String] {
      def read(s: String): Option[String] =
        if (s.headOption.exists(_ == '"') && s.lastOption.exists(_ == '"')) Some(s.drop(1).dropRight(1)) else None
    }
    implicit val readInt: Read[Int] = new Read[Int] { def read(s: String): Option[Int] = s.parseInt.toOption }
    implicit val readBool: Read[Boolean] = new Read[Boolean] { def read(s: String): Option[Boolean] = s.parseBoolean.toOption }

    trait ReadAp {
      def ap[A, B](fa: => Read[A])(f: => Read[A => B]): Read[B] =
        new Read[B] { def read(s: String): Option[B] = (f.read(s) |@| fa.read(s))(_(_)) }
    }

    implicit val readApply: Apply[Read] = new Apply[Read] with ReadAp {
      def map[A, B](fa: Read[A])(f: A => B): Read[B] = new Read[B] { def read(s: String): Option[B] = fa.read(s).map(f) }
    }

    implicit val readAlt: Alt[Read] = new Alt[Read] with ReadAp {
      def point[A](a: => A): Read[A] = new Read[A] { def read(s: String): Option[A] = Some(a) }
      def alt[A](a1: => Read[A], a2: => Read[A]): Read[A] =
        new Read[A] { def read(s: String): Option[A] = a1.read(s).orElse(a2.read(s)) }
    }
  }

  trait Csv[A] { def toCsv(a: A): List[String] }
  object Csv {
    implicit val csvStr: Csv[String] = new Csv[String] { def toCsv(x: String): List[String] = List(x) }
    implicit val csvInt: Csv[Int] = new Csv[Int] { def toCsv(x: Int): List[String] = List(x.toString) }
    implicit val csvBool: Csv[Boolean] = new Csv[Boolean] { def toCsv(x: Boolean): List[String] = List(x.toString) }
    implicit def csvList[A](implicit c: Csv[A]): Csv[List[A]] =
      new Csv[List[A]] { def toCsv(l: List[A]): List[String] = l.flatMap(c.toCsv(_)) }

    implicit def csvOption[A](implicit c: Csv[A]): Csv[Option[A]] =
      new Csv[Option[A]] { def toCsv(o: Option[A]): List[String] = o.fold(List[String](null))(c.toCsv(_)) }

    implicit def csvLabelled[A: Csv, L <: Singleton with String](implicit c: Csv[A]): Csv[Labelled.Aux[A, L]] =
      new Csv[Labelled.Aux[A, L]] {
        def toCsv(a: Labelled.Aux[A, L]): List[String] = c.toCsv(a.value)
      }

    implicit def csvAdtVal[A <: Singleton, L <: Singleton with String]: Csv[Labelled.Aux[ADTValue[A], L]] =
      new Csv[Labelled.Aux[ADTValue[A], L]] {
        def toCsv(a: Labelled.Aux[ADTValue[A], L]): List[String] = List(a.label)
      }

    type CsvF[A] = A => List[String]
    implicit val csvIso: IsoFunctor[Csv, CsvF] =
      IsoFunctor[Csv, CsvF](Lambda[Csv ~> CsvF](_.toCsv _), new (CsvF ~> Csv) {
        def apply[A](f: CsvF[A]): Csv[A] =
          new Csv[A] { def toCsv(a: A): List[String] = f(a) }
      })

    implicit val csvDecide: Decidable[Csv] = Decidable.fromIso[Csv, CsvF](csvIso)
    implicit val csvDivide: Divide[Csv] = Divide.fromIso[Csv, CsvF](csvIso)
  }
}

object types {
  import typeclasses._

  @deriving case class NoInstances(s: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  sealed trait Foo
  case object Bar extends Foo
  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Baz(s: String) extends Foo

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  sealed trait Trait0
  sealed trait Trait1 extends Trait0 { val value: Option[Int] }
  sealed trait Trait2 extends Trait0
  sealed trait Trait3 extends Trait1
  case object Inst1 extends Trait0
  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Inst2(value: Option[Int]) extends Trait1
  case object Inst3 extends Trait1 { val value = Some(3) }
  case object Inst4 extends Trait2
  case object Inst5 extends Trait3 { val value = Some(5) }
  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Inst6(x: String) extends Trait3 { val value = x.parseInt.toOption }

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Multi(str: String)(val int: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HKFG[F[_[_]], G[_]](run: F[G])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Covariant[+A](a: A)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Contravariant[-A](s: String, i: Int) {
    def go(a: A): (String, Int) = (s"$s -- $a", i)
  }

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class ErrorTest1[A](i: Int, a: A)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class ErrorTest2[A](as: List[A], ints: List[Int], test1s: List[ErrorTest1[A]])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class ErrorTest3[D[_], A](test1s: List[ErrorTest1[D[A]]], test2: ErrorTest2[A])
  object ErrorTest3 {
    def update[G[_], B](bs: List[B], ts: List[(Int, B)], d: List[Int]): ErrorTest3[G, B] =
      ErrorTest3(Nil, ErrorTest2(bs, d, ts.map(t => ErrorTest1[B](t._1, t._2))))
  }

  @newtype
  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test1(x0: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams1[A1](x0: A1)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK1[F[_], A1](run: F[A1])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test2(x0: Int, x1: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams2[A1, A2](x0: A1, x1: A2)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK2[F[_, _], A1, A2](run: F[A1, A2])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test3(x0: Int, x1: String, x2: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams3[A1, A2, A3](x0: A1, x1: A2, x2: A3)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK3[F[_, _, _], A1, A2, A3](run: F[A1, A2, A3])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test4(x0: Int, x1: String, x2: Boolean, x3: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams4[A1, A2, A3, A4](x0: A1, x1: A2, x2: A3, x3: A4)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK4[F[_, _, _, _], A1, A2, A3, A4](run: F[A1, A2, A3, A4])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test5(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams5[A1, A2, A3, A4, A5](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK5[F[_, _, _, _, _], A1, A2, A3, A4, A5](run: F[A1, A2, A3, A4, A5])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test6(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams6[A1, A2, A3, A4, A5, A6](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK6[F[_, _, _, _, _, _], A1, A2, A3, A4, A5, A6](run: F[A1, A2, A3, A4, A5, A6])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test7(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams7[A1, A2, A3, A4, A5, A6, A7](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK7[F[_, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7](run: F[A1, A2, A3, A4, A5, A6, A7])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test8(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams8[A1, A2, A3, A4, A5, A6, A7, A8](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK8[F[_, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8](run: F[A1, A2, A3, A4, A5, A6, A7, A8])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test9(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams9[A1, A2, A3, A4, A5, A6, A7, A8, A9](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK9[F[_, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test10(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK10[F[_, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test11(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK11[F[_, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test12(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK12[F[_, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test13(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK13[F[_, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test14(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK14[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test15(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK15[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test16(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK16[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test17(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int, x16: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams17[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16, x16: A17)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK17[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test18(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int, x16: String, x17: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams18[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16, x16: A17, x17: A18)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK18[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test19(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int, x16: String, x17: Boolean, x18: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams19[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16, x16: A17, x17: A18, x18: A19)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK19[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test20(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int, x16: String, x17: Boolean, x18: Int, x19: String)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams20[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16, x16: A17, x17: A18, x18: A19, x19: A20)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK20[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test21(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int, x16: String, x17: Boolean, x18: Int, x19: String, x20: Boolean)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16, x16: A17, x17: A18, x18: A19, x19: A20, x20: A21)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK21[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21])

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class Test22(x0: Int, x1: String, x2: Boolean, x3: Int, x4: String, x5: Boolean, x6: Int, x7: String, x8: Boolean, x9: Int, x10: String, x11: Boolean, x12: Int, x13: String, x14: Boolean, x15: Int, x16: String, x17: Boolean, x18: Int, x19: String, x20: Boolean, x21: Int)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class TParams22[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22](x0: A1, x1: A2, x2: A3, x3: A4, x4: A5, x5: A6, x6: A7, x7: A8, x8: A9, x9: A10, x10: A11, x11: A12, x12: A13, x13: A14, x14: A15, x15: A16, x16: A17, x17: A18, x18: A19, x19: A20, x20: A21, x21: A22)

  @deriving(Arbitrary, Csv, Decoder, DecodeJson, Encoder, EncodeJson, Equal, Read, Show)
  case class HK22[F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22](run: F[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22])

}

object DerivingPluginTest extends Properties("DerivingPlugin") {
  import typeclasses.{Csv, Read}
  import types._

  def proof[A: Arbitrary: Csv: DecodeJson: Decoder: EncodeJson: Encoder: Equal: Read: Show](label: String) =
    property(label) = forAllNoShrink((a: A) => {
      (implicitly[Csv[A]].toCsv(a).nonEmpty :| "CSV output was empty") &&
        ((implicitly[DecodeJson[A]].decodeJson(implicitly[EncodeJson[A]].encode(a)).toOption.get === a) :| "argonaut was not equal") &&
        ((implicitly[Decoder[A]].decodeJson(implicitly[Encoder[A]].apply(a)) match {
          case Right(res) => res === a
          case _ => false
        }) :| "circe was not equal") &&
        // can't really test `Read` because the implementations don't work for nested values
        (implicitly[Show[A]].shows(a).nonEmpty :| "show/read was not equal")
    })

  proof[Foo]("Foo")
  proof[Baz]("Baz")
  proof[Trait0]("Trait0")
  proof[Multi]("Multi")
  proof[HKFG[FConst[String]#T, Id]]("HKFG")
  proof[Covariant[String]]("Covariant")
  proof[Contravariant[String]]("Contravariant")

  proof[Test1](" Test1 ")
  proof[TParams1[String]](" TParams1 ")
  proof[HK1[TParams1, String]](" HK1 ")

  proof[Test2](" Test2 ")
  proof[TParams2[String, String]](" TParams2 ")
  proof[HK2[TParams2, String, String]](" HK2 ")

  proof[Test3](" Test3 ")
  proof[TParams3[String, String, String]](" TParams3 ")
  proof[HK3[TParams3, String, String, String]](" HK3 ")

  proof[Test4](" Test4 ")
  proof[TParams4[String, String, String, String]](" TParams4 ")
  proof[HK4[TParams4, String, String, String, String]](" HK4 ")

  proof[Test5](" Test5 ")
  proof[TParams5[String, String, String, String, String]](" TParams5 ")
  proof[HK5[TParams5, String, String, String, String, String]](" HK5 ")

  proof[Test6](" Test6 ")
  proof[TParams6[String, String, String, String, String, String]](" TParams6 ")
  proof[HK6[TParams6, String, String, String, String, String, String]](" HK6 ")

  proof[Test7](" Test7 ")
  proof[TParams7[String, String, String, String, String, String, String]](" TParams7 ")
  proof[HK7[TParams7, String, String, String, String, String, String, String]](" HK7 ")

  proof[Test8](" Test8 ")
  proof[TParams8[String, String, String, String, String, String, String, String]](" TParams8 ")
  proof[HK8[TParams8, String, String, String, String, String, String, String, String]](" HK8 ")

  proof[Test9](" Test9 ")
  proof[TParams9[String, String, String, String, String, String, String, String, String]](" TParams9 ")
  proof[HK9[TParams9, String, String, String, String, String, String, String, String, String]](" HK9 ")

  proof[Test10](" Test10 ")
  proof[TParams10[String, String, String, String, String, String, String, String, String, String]](" TParams10 ")
  proof[HK10[TParams10, String, String, String, String, String, String, String, String, String, String]](" HK10 ")

  proof[Test11](" Test11 ")
  proof[TParams11[String, String, String, String, String, String, String, String, String, String, String]](" TParams11 ")
  proof[HK11[TParams11, String, String, String, String, String, String, String, String, String, String, String]](" HK11 ")

  proof[Test12](" Test12 ")
  proof[TParams12[String, String, String, String, String, String, String, String, String, String, String, String]](" TParams12 ")
  proof[HK12[TParams12, String, String, String, String, String, String, String, String, String, String, String, String]](" HK12 ")

  proof[Test13](" Test13 ")
  proof[TParams13[String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams13 ")
  proof[HK13[TParams13, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK13 ")

  proof[Test14](" Test14 ")
  proof[TParams14[String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams14 ")
  proof[HK14[TParams14, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK14 ")

  proof[Test15](" Test15 ")
  proof[TParams15[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams15 ")
  proof[HK15[TParams15, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK15 ")

  proof[Test16](" Test16 ")
  proof[TParams16[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams16 ")
  proof[HK16[TParams16, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK16 ")

  proof[Test17](" Test17 ")
  proof[TParams17[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams17 ")
  proof[HK17[TParams17, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK17 ")

  proof[Test18](" Test18 ")
  proof[TParams18[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams18 ")
  proof[HK18[TParams18, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK18 ")

  proof[Test19](" Test19 ")
  proof[TParams19[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams19 ")
  proof[HK19[TParams19, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK19 ")

  proof[Test20](" Test20 ")
  proof[TParams20[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams20 ")
  proof[HK20[TParams20, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK20 ")

  proof[Test21](" Test21 ")
  proof[TParams21[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams21 ")
  proof[HK21[TParams21, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK21 ")

  proof[Test22](" Test22 ")
  proof[TParams22[String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" TParams22 ")
  proof[HK22[TParams22, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String]](" HK22 ")

}
