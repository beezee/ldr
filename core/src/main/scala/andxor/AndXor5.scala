package andxor

import andxor.syntax.ffunctor._
import andxor.syntax.ftraverse._
import andxor.types._
import scalaz.{~>, Applicative, Functor, PlusEmpty, Monoid}
import scalaz.Id.Id
import scalaz.std.vector._

trait AndXorNested5[A1[_[_]], A2[_[_]], A3[_[_]], A4[_[_]], A5[_[_]]] extends AndXor {

  def apply[B1]: AndXorNested6[A1, A2, A3, A4, A5, FConst[B1]#T] = AndXorNested6[A1, A2, A3, A4, A5, FConst[B1]#T]
  def nest[B1[_[_]]]: AndXorNested6[A1, A2, A3, A4, A5, B1] = AndXorNested6[A1, A2, A3, A4, A5, B1]

  type Prod[F[_]] = Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]
  object Prod {
    def apply[F[_]](p: (A1[F], A2[F], A3[F], A4[F], A5[F])): Prod[F] = Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]](p)
  }

  type Cop[F[_]] = Cop5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]
  object Cop {
    def apply[F[_]](c: Either[A1[F], Either[A2[F], Either[A3[F], Either[A4[F], A5[F]]]]]): Cop[F] = Cop5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]](c)
  }

  object instances {
    implicit def axoProd5Instance(implicit ft0: FTraverse[A1, Applicative], ft1: FTraverse[A2, Applicative], ft2: FTraverse[A3, Applicative], ft3: FTraverse[A4, Applicative], ft4: FTraverse[A5, Applicative]): FFunctor[Prod] with FTraverseProd[Prod] =
      new FFunctor[Prod] with FTraverseProd[Prod] {
        def map[F[_], G[_]](p: Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]])(nt: F ~> G): Prod5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]] =
          Prod5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]]((ft0.map(p.t1)(nt), ft1.map(p.t2)(nt), ft2.map(p.t3)(nt), ft3.map(p.t4)(nt), ft4.map(p.t5)(nt)))

        def traverse[F[_], G[_], A[_]: Applicative](p: Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]])(f: F ~> Lambda[a => A[G[a]]]): A[Prod5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]]] =
          Applicative[A].ap(ft4.traverse(p.t5)(f))(Applicative[A].ap(ft3.traverse(p.t4)(f))(Applicative[A].ap(ft2.traverse(p.t3)(f))(Applicative[A].ap(ft1.traverse(p.t2)(f))(Applicative[A].map(ft0.traverse(p.t1)(f))((i0: A1[G]) => (i1: A2[G]) => (i2: A3[G]) => (i3: A4[G]) => (i4: A5[G]) => Prod5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]]((i0, i1, i2, i3, i4)))))))
      }

    implicit def axoProd5FoldMap(implicit fm0: FoldMap[A1, A1], fm1: FoldMap[A2, A2], fm2: FoldMap[A3, A3], fm3: FoldMap[A4, A4], fm4: FoldMap[A5, A5]): FoldMap[Prod, Cop] =
      new FoldMap[Prod, Cop] {
        def emptyProd[F[_]](implicit PE: PlusEmpty[F]): Prod[F] =
          Prod((fm0.emptyProd, fm1.emptyProd, fm2.emptyProd, fm3.emptyProd, fm4.emptyProd))

        def unconsAll[F[_], G[_]](p: Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]])(implicit U: Uncons[F, G]): (List[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]]], Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]) = {
          val (h1, t1) = fm0.unconsAll(p.t1)
          val (h2, t2) = fm1.unconsAll(p.t2)
          val (h3, t3) = fm2.unconsAll(p.t3)
          val (h4, t4) = fm3.unconsAll(p.t4)
          val (h5, t5) = fm4.unconsAll(p.t5)
          (
            List(h1.map(Inj[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]], A1[G]].apply(_)), h2.map(Inj[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]], A2[G]].apply(_)), h3.map(Inj[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]], A3[G]].apply(_)), h4.map(Inj[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]], A4[G]].apply(_)), h5.map(Inj[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]], A5[G]].apply(_))).flatten,
            Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((t1, t2, t3, t4, t5)))
        }

        def unconsOne[F[_], G[_]](p: Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]], c: Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]])(implicit U: Uncons[F, G]): (Option[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]]], Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]) =
          c.run match {

            case Left(x) =>
              val (h, t) = fm0.unconsOne(p.t1, x)
              (h.map(v => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Left(v))), Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((t, p.t2, p.t3, p.t4, p.t5)))

            case Right(Left(x)) =>
              val (h, t) = fm1.unconsOne(p.t2, x)
              (h.map(v => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Left(v)))), Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((p.t1, t, p.t3, p.t4, p.t5)))

            case Right(Right(Left(x))) =>
              val (h, t) = fm2.unconsOne(p.t3, x)
              (h.map(v => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Right(Left(v))))), Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((p.t1, p.t2, t, p.t4, p.t5)))

            case Right(Right(Right(Left(x)))) =>
              val (h, t) = fm3.unconsOne(p.t4, x)
              (h.map(v => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Right(Right(Left(v)))))), Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((p.t1, p.t2, p.t3, t, p.t5)))

            case Right(Right(Right(Right(x)))) =>
              val (h, t) = fm4.unconsOne(p.t5, x)
              (h.map(v => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Right(Right(Right(v)))))), Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((p.t1, p.t2, p.t3, p.t4, t)))

          }
      }

    implicit def axoCop5Instance(implicit ft0: FTraverse[A1, Functor], ft1: FTraverse[A2, Functor], ft2: FTraverse[A3, Functor], ft3: FTraverse[A4, Functor], ft4: FTraverse[A5, Functor]): FFunctor[Cop] with FTraverseCop[Cop] =
      new FFunctor[Cop] with FTraverseCop[Cop] {
        def map[F[_], G[_]](c: Cop5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]])(nt: F ~> G): Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]] =
          Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](c.run.bimap(_.map(nt), _.bimap(_.map(nt), _.bimap(_.map(nt), _.bimap(_.map(nt), _.map(nt))))))

        def traverse[F[_], G[_], A[_]: Functor](c: Cop5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]])(f: F ~> Lambda[a => A[G[a]]]): A[Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]]] =
          c.run match {

            case Left(x) => Functor[A].map(x.traverse(f))(y => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Left(y)))

            case Right(Left(x)) => Functor[A].map(x.traverse(f))(y => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Left(y))))

            case Right(Right(Left(x))) => Functor[A].map(x.traverse(f))(y => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Right(Left(y)))))

            case Right(Right(Right(Left(x)))) => Functor[A].map(x.traverse(f))(y => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Right(Right(Left(y))))))

            case Right(Right(Right(Right(x)))) => Functor[A].map(x.traverse(f))(y => Cop5[Id, A1[G], A2[G], A3[G], A4[G], A5[G]](Right(Right(Right(Right(y))))))

          }
      }
  }

  def deriving[TC[_], F[_]](implicit t0: TC[A1[F]], t1: TC[A2[F]], t2: TC[A3[F]], t3: TC[A4[F]], t4: TC[A5[F]]): AndXorDeriving[TC, Cop[F], Prod[F]] =

    new AndXorDeriving[TC, Cop[F], Prod[F]] {
      def mkChoose[B](f: B => Cop[F])(implicit d: Decidable[TC]): TC[B] =
        Combine.choose5(t0, t1, t2, t3, t4)(f(_).run)

      def mkAlt[B](f: Cop[F] => B)(implicit a: Alt[TC]): TC[B] =
        Combine.altly5(t0, t1, t2, t3, t4)(x => f(Cop5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]](x)))

      def mkDivide[B](f: B => Prod[F])(implicit a: Divide[TC]): TC[B] =
        Combine.divide5(t0, t1, t2, t3, t4)(f(_).run)

      def mkApply[B](f: Prod[F] => B)(implicit a: Apply[TC]): TC[B] =

        Combine.apply5(t0, t1, t2, t3, t4) {
          case (i0, i1, i2, i3, i4) =>
            f(Prod5[Id, A1[F], A2[F], A3[F], A4[F], A5[F]]((i0, i1, i2, i3, i4)))
        }

    }

  def derivingId[TC[_]](implicit t0: TC[A1[Id]], t1: TC[A2[Id]], t2: TC[A3[Id]], t3: TC[A4[Id]], t4: TC[A5[Id]]): AndXorDeriving[TC, Cop[Id], Prod[Id]] = deriving[TC, Id]

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit def injEv[F[_]]: Inj[Cop[F], Cop[F]] = deriving[Inj[Cop[F], ?], F].choose
    implicit def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = deriving[Inj[Prod[F], ?], F].divide
    implicit def injCopToProdEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Cop[F]] = deriving[Inj[Prod[F], ?], F].choose
    implicit def injProdToVecCopEv[F[_]]: Inj[Vector[Cop[F]], Prod[F]] = deriving[Inj[Vector[Cop[F]], ?], F].divide
  }

}

object AndXorNested5 {
  def apply[A1[_[_]], A2[_[_]], A3[_[_]], A4[_[_]], A5[_[_]]]: AndXorNested5[A1, A2, A3, A4, A5] =
    new AndXorNested5[A1, A2, A3, A4, A5] {}
}

trait AndXor5[A1, A2, A3, A4, A5] extends AndXor {

  def apply[B1]: AndXor6[A1, A2, A3, A4, A5, B1] = AndXor6[A1, A2, A3, A4, A5, B1]
  def nest[B1[_[_]]]: AndXorNested6[FConst[A1]#T, FConst[A2]#T, FConst[A3]#T, FConst[A4]#T, FConst[A5]#T, B1] = AndXorNested6[FConst[A1]#T, FConst[A2]#T, FConst[A3]#T, FConst[A4]#T, FConst[A5]#T, B1]

  type Prod[F[_]] = Prod5[F, A1, A2, A3, A4, A5]
  object Prod {
    def apply[F[_]](p: (F[A1], F[A2], F[A3], F[A4], F[A5])): Prod[F] = Prod5[F, A1, A2, A3, A4, A5](p)
  }

  type Cop[F[_]] = Cop5[F, A1, A2, A3, A4, A5]
  object Cop {
    def apply[F[_]](c: Either[F[A1], Either[F[A2], Either[F[A3], Either[F[A4], F[A5]]]]]): Cop[F] = Cop5[F, A1, A2, A3, A4, A5](c)
  }

  def deriving[TC[_], F[_]](implicit t0: TC[F[A1]], t1: TC[F[A2]], t2: TC[F[A3]], t3: TC[F[A4]], t4: TC[F[A5]]): AndXorDeriving[TC, Cop[F], Prod[F]] =

    new AndXorDeriving[TC, Cop[F], Prod[F]] {
      def mkChoose[B](f: B => Cop[F])(implicit d: Decidable[TC]): TC[B] =
        Combine.choose5(t0, t1, t2, t3, t4)(f(_).run)

      def mkAlt[B](f: Cop[F] => B)(implicit a: Alt[TC]): TC[B] =
        Combine.altly5(t0, t1, t2, t3, t4)(x => f(Cop5[F, A1, A2, A3, A4, A5](x)))

      def mkDivide[B](f: B => Prod[F])(implicit a: Divide[TC]): TC[B] =
        Combine.divide5(t0, t1, t2, t3, t4)(f(_).run)

      def mkApply[B](f: Prod[F] => B)(implicit a: Apply[TC]): TC[B] =

        Combine.apply5(t0, t1, t2, t3, t4) {
          case (i0, i1, i2, i3, i4) =>
            f(Prod5[F, A1, A2, A3, A4, A5]((i0, i1, i2, i3, i4)))
        }

    }

  def derivingId[TC[_]](implicit t0: TC[A1], t1: TC[A2], t2: TC[A3], t3: TC[A4], t4: TC[A5]): AndXorDeriving[TC, Cop[Id], Prod[Id]] = deriving[TC, Id]

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit def injEv[F[_]]: Inj[Cop[F], Cop[F]] = deriving[Inj[Cop[F], ?], F].choose
    implicit def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = deriving[Inj[Prod[F], ?], F].divide
    implicit def injCopToProdEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Cop[F]] = deriving[Inj[Prod[F], ?], F].choose
    implicit def injProdToVecCopEv[F[_]]: Inj[Vector[Cop[F]], Prod[F]] = deriving[Inj[Vector[Cop[F]], ?], F].divide
  }

}

object AndXor5 {
  def apply[A1, A2, A3, A4, A5]: AndXor5[A1, A2, A3, A4, A5] =
    new AndXor5[A1, A2, A3, A4, A5] {}
}
