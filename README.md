Inspired by scalaz-deriving.

Provides macroless composition of covariant or contravariant
typeclasses for product and coproduct types.

Given a set of types, produces a representation of Coproduct as nested disjunctions,
as well as Product as a tuple. Also provides an `inj` method for lifting a compatible type
into a Coproduct representation, and a `lift` method for lifting a compatible type
into a Product representation when that Product is monoidal.

With an instance of Decidable, Alt, Divide, or Apply for a given typeclass,
provides an instance for the corresponding Coproduct, or Product respectively.

```scala
scala> import andxor.{AndXor, Decidable}
import andxor.{AndXor, Decidable}

scala> import scalaz.std.anyVal._
import scalaz.std.anyVal._

scala> import scalaz.std.list._
import scalaz.std.list._

scala> import scalaz.std.option._
import scalaz.std.option._

scala> import scalaz.std.string._
import scalaz.std.string._

scala> import scalaz.std.tuple._
import scalaz.std.tuple._

scala> import scalaz.syntax.monoid._
import scalaz.syntax.monoid._

scala> import scalaz.syntax.either._
import scalaz.syntax.either._

scala> import scalaz.{Show, \/, ~>}
import scalaz.{Show, $bslash$div, $tilde$greater}

scala> val SIS = AndXor.build[String, Int, List[String]]
SIS: andxor.AndXor3[String,Int,List[String]] = andxor.AndXor3$$anon$5@6ad02348

scala> implicit val ds: Decidable[Show] = new Decidable[Show] {
     |   def choose2[Z, A1, A2](a1: => Show[A1], a2: =>Show[A2])(f: Z => (A1 \/ A2)): Show[Z] =
     |     Show.show[Z]((z: Z) => f(z).fold(a1.show(_), a2.show(_)))
     | 
     |   def contramap[A, B](fa: Show[A])(f: B => A): Show[B] =
     |     Show.show[B]((b: B) => fa.show(f(b)))
     | }
ds: andxor.Decidable[scalaz.Show] = $anon$1@1b2f91b

scala> SIS.combine[Show].choose.show(SIS.inj("foo": scalaz.Id.Id[String]))
res0: scalaz.Cord = "foo"

scala> SIS.combine[Show].choose.show(SIS.inj(2))
res1: scalaz.Cord = 2

scala> SIS.combine[Show].choose.show(SIS.inj(List("bar", "baz")))
res2: scalaz.Cord = ["bar","baz"]

scala> // lift into monoidal product
     | val SISF = AndXor.buildF[String, Int, List[String]]
SISF: andxor.AndXorF3[String,Int,List[String]] = andxor.AndXorF3$$anon$4@24a1642

scala> val SISL = SISF[List]
SISL: SISF.Repr[List] = andxor.AndXorF3$$anon$3@35d4ec3c

scala> val ls = SISL.lift(List(4)) |+| SISL.lift(List("foo")) |+| SISL.lift(List(List("bar")))
ls: SISL.Prod = (List(foo),List(4),List(List(bar)))

scala> val SISO = SISF[Option]
SISO: SISF.Repr[Option] = andxor.AndXorF3$$anon$3@24e84d9b

scala> val os = SISO.lift(Option(4)) |+| SISO.lift(Option("foo")) |+| SISO.lift(Option(List("bar")))
os: SISO.Prod = (Some(foo),Some(4),Some(List(bar)))

scala> // convert between F[_]s using a ~>
     | val l2o = new (List ~> Option) { def apply[A](l: List[A]): Option[A] = l.headOption }
l2o: List ~> Option = $anon$1@7db789d5

scala> SISL.transformP(l2o)(ls)
res5: andxor.types.Prod3.Type[Option,String,Int,List[String]] = (Some(foo),Some(4),Some(List(bar)))

scala> val o2l = new (Option ~> List) { def apply[A](o: Option[A]): List[A] = o.toList }
o2l: Option ~> List = $anon$1@484d3fb6

scala> SISO.transformP(o2l)(os)
res6: andxor.types.Prod3.Type[List,String,Int,List[String]] = (List(foo),List(4),List(List(bar)))

scala> // sequence Cop or Prod to Id
     | SISO.sequenceC(SISO.inj(Option("foo")))
res8: Option[andxor.types.Cop3[scalaz.Id.Id,String,Int,List[String]]] = Some(-\/(foo))

scala> SISO.sequenceP(SISO.Prod((Option("foo"), Option(1), Option(List("bar")))))
res9: Option[andxor.types.Prod3[scalaz.Id.Id,String,Int,List[String]]] = Some((foo,1,List(bar)))

scala> // map given index of Cop or Prod
     | import andxor.MapN.syntax._
import andxor.MapN.syntax._

scala> SISO.inj(Option(2)).run.map1(_.map(_.length)).map2(_.map(_.toString ++ "!"))
res11: Option[Int] \/ (Option[String] \/ Option[List[String]]) = \/-(-\/(Some(2!)))

scala> SISO.lift(Option("foo")).run.map2(_.map(_.toString ++ "!")).map1(_.map(_.length))
res12: (Option[Int], Option[String], Option[List[String]]) = (Some(3),None,None)

scala> // extract specific type from Cop or Prod
     | SISO.extractC[Option[String]](SISO.inj(Option("foo")))
res14: Option[Option[String]] = Some(Some(foo))

scala> SISO.extractC[Option[Int]](SISO.inj(Option("foo")))
res15: Option[Option[Int]] = None

scala> SISO.extractP[Option[String]](SISO.lift(Option("foo")))
res16: Option[String] = Some(foo)

scala> SISO.extractP[Option[Int]](SISO.lift(Option(1)))
res17: Option[Int] = Some(1)

scala> // substitute F[_] for G[_] at a specific index
     | SIS.subst2[Option]
res19: andxor.AndXor3[scalaz.Id.Id[String],Option[Int],scalaz.Id.Id[List[String]]] = andxor.AndXor3$$anon$5@64c89334
```
