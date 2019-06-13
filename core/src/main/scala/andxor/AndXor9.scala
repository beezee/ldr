package andxor

import andxor.types.{Cop9, Prod9}
import scalaz.{Apply, Monoid, \/}
import scalaz.Id.Id

trait AndXor9[A1 <: AndXor, A2 <: AndXor, A3 <: AndXor, A4 <: AndXor, A5 <: AndXor, A6 <: AndXor, A7 <: AndXor, A8 <: AndXor, A9 <: AndXor] extends AndXor {
  type Prod[F[_]] = Prod9[F, A1, A2, A3, A4, A5, A6, A7, A8, A9]
  object Prod {
    def apply[F[_]](p: (A1#Prod[F], A2#Prod[F], A3#Prod[F], A4#Prod[F], A5#Prod[F], A6#Prod[F], A7#Prod[F], A8#Prod[F], A9#Prod[F])): Prod[F] = Prod9[F, A1, A2, A3, A4, A5, A6, A7, A8, A9](p)
  }

  type Cop[F[_]] = Cop9[F, A1, A2, A3, A4, A5, A6, A7, A8, A9]
  object Cop {
    def apply[F[_]](c: (A1#Cop[F] \/ (A2#Cop[F] \/ (A3#Cop[F] \/ (A4#Cop[F] \/ (A5#Cop[F] \/ (A6#Cop[F] \/ (A7#Cop[F] \/ (A8#Cop[F] \/ A9#Cop[F]))))))))): Cop[F] = Cop9[F, A1, A2, A3, A4, A5, A6, A7, A8, A9](c)
  }

  def mkChoose[TC[_], F[_], B](f: B => Cop[F])(implicit d: Decidable[TC], a0: TC[A1#Cop[F]], a1: TC[A2#Cop[F]], a2: TC[A3#Cop[F]], a3: TC[A4#Cop[F]], a4: TC[A5#Cop[F]], a5: TC[A6#Cop[F]], a6: TC[A7#Cop[F]], a7: TC[A8#Cop[F]], a8: TC[A9#Cop[F]]): TC[B] =
    Combine.choose9(a0, a1, a2, a3, a4, a5, a6, a7, a8)(f(_).run)

  def mkAlt[TC[_], F[_], B](f: Cop[F] => B)(implicit a: Alt[TC], a0: TC[A1#Cop[F]], a1: TC[A2#Cop[F]], a2: TC[A3#Cop[F]], a3: TC[A4#Cop[F]], a4: TC[A5#Cop[F]], a5: TC[A6#Cop[F]], a6: TC[A7#Cop[F]], a7: TC[A8#Cop[F]], a8: TC[A9#Cop[F]]): TC[B] =
    Combine.altly9(a0, a1, a2, a3, a4, a5, a6, a7, a8)(x => f(Cop(x)))

  def mkDivide[TC[_], F[_], B](f: B => Prod[F])(implicit d: Divide[TC], a0: TC[A1#Prod[F]], a1: TC[A2#Prod[F]], a2: TC[A3#Prod[F]], a3: TC[A4#Prod[F]], a4: TC[A5#Prod[F]], a5: TC[A6#Prod[F]], a6: TC[A7#Prod[F]], a7: TC[A8#Prod[F]], a8: TC[A9#Prod[F]]): TC[B] =
    Combine.divide9(a0, a1, a2, a3, a4, a5, a6, a7, a8)(f(_).run)

  def mkApply[TC[_], F[_], B](f: Prod[F] => B)(implicit a: Apply[TC], a0: TC[A1#Prod[F]], a1: TC[A2#Prod[F]], a2: TC[A3#Prod[F]], a3: TC[A4#Prod[F]], a4: TC[A5#Prod[F]], a5: TC[A6#Prod[F]], a6: TC[A7#Prod[F]], a7: TC[A8#Prod[F]], a8: TC[A9#Prod[F]]): TC[B] =

    Combine.apply9(a0, a1, a2, a3, a4, a5, a6, a7, a8) {
      case (i0, i1, i2, i3, i4, i5, i6, i7, i8) =>
        f(Prod((i0, i1, i2, i3, i4, i5, i6, i7, i8)))
    }

  def mkChoose[TC[_], B](f: B => Cop[Id])(implicit d: Decidable[TC], a0: TC[A1#Cop[Id]], a1: TC[A2#Cop[Id]], a2: TC[A3#Cop[Id]], a3: TC[A4#Cop[Id]], a4: TC[A5#Cop[Id]], a5: TC[A6#Cop[Id]], a6: TC[A7#Cop[Id]], a7: TC[A8#Cop[Id]], a8: TC[A9#Cop[Id]], dummy: DummyImplicit): TC[B] = mkChoose[TC, Id, B](f)
  def mkAlt[TC[_], B](f: Cop[Id] => B)(implicit a: Alt[TC], a0: TC[A1#Cop[Id]], a1: TC[A2#Cop[Id]], a2: TC[A3#Cop[Id]], a3: TC[A4#Cop[Id]], a4: TC[A5#Cop[Id]], a5: TC[A6#Cop[Id]], a6: TC[A7#Cop[Id]], a7: TC[A8#Cop[Id]], a8: TC[A9#Cop[Id]], dummy: DummyImplicit): TC[B] = mkAlt[TC, Id, B](f)
  def mkDivide[TC[_], B](f: B => Prod[Id])(implicit d: Divide[TC], a0: TC[A1#Prod[Id]], a1: TC[A2#Prod[Id]], a2: TC[A3#Prod[Id]], a3: TC[A4#Prod[Id]], a4: TC[A5#Prod[Id]], a5: TC[A6#Prod[Id]], a6: TC[A7#Prod[Id]], a7: TC[A8#Prod[Id]], a8: TC[A9#Prod[Id]], dummy: DummyImplicit): TC[B] = mkDivide[TC, Id, B](f)
  def mkApply[TC[_], B](f: Prod[Id] => B)(implicit a: Apply[TC], a0: TC[A1#Prod[Id]], a1: TC[A2#Prod[Id]], a2: TC[A3#Prod[Id]], a3: TC[A4#Prod[Id]], a4: TC[A5#Prod[Id]], a5: TC[A6#Prod[Id]], a6: TC[A7#Prod[Id]], a7: TC[A8#Prod[Id]], a8: TC[A9#Prod[Id]], dummy: DummyImplicit): TC[B] = mkApply[TC, Id, B](f)

  def choose[TC[_], F[_]](implicit d: Decidable[TC], a0: TC[A1#Cop[F]], a1: TC[A2#Cop[F]], a2: TC[A3#Cop[F]], a3: TC[A4#Cop[F]], a4: TC[A5#Cop[F]], a5: TC[A6#Cop[F]], a6: TC[A7#Cop[F]], a7: TC[A8#Cop[F]], a8: TC[A9#Cop[F]]): TC[Cop[F]] = mkChoose[TC, F, Cop[F]](identity)
  def alt[TC[_], F[_]](implicit a: Alt[TC], a0: TC[A1#Cop[F]], a1: TC[A2#Cop[F]], a2: TC[A3#Cop[F]], a3: TC[A4#Cop[F]], a4: TC[A5#Cop[F]], a5: TC[A6#Cop[F]], a6: TC[A7#Cop[F]], a7: TC[A8#Cop[F]], a8: TC[A9#Cop[F]]): TC[Cop[F]] = mkAlt[TC, F, Cop[F]](identity)
  def divide[TC[_], F[_]](implicit d: Divide[TC], a0: TC[A1#Prod[F]], a1: TC[A2#Prod[F]], a2: TC[A3#Prod[F]], a3: TC[A4#Prod[F]], a4: TC[A5#Prod[F]], a5: TC[A6#Prod[F]], a6: TC[A7#Prod[F]], a7: TC[A8#Prod[F]], a8: TC[A9#Prod[F]]): TC[Prod[F]] = mkDivide[TC, F, Prod[F]](identity)
  def apply[TC[_], F[_]](implicit a: Apply[TC], a0: TC[A1#Prod[F]], a1: TC[A2#Prod[F]], a2: TC[A3#Prod[F]], a3: TC[A4#Prod[F]], a4: TC[A5#Prod[F]], a5: TC[A6#Prod[F]], a6: TC[A7#Prod[F]], a7: TC[A8#Prod[F]], a8: TC[A9#Prod[F]]): TC[Prod[F]] = mkApply[TC, F, Prod[F]](identity)

  def choose[TC[_]](implicit d: Decidable[TC], a0: TC[A1#Cop[Id]], a1: TC[A2#Cop[Id]], a2: TC[A3#Cop[Id]], a3: TC[A4#Cop[Id]], a4: TC[A5#Cop[Id]], a5: TC[A6#Cop[Id]], a6: TC[A7#Cop[Id]], a7: TC[A8#Cop[Id]], a8: TC[A9#Cop[Id]], dummy: DummyImplicit): TC[Cop[Id]] = mkChoose[TC, Cop[Id]](identity)
  def alt[TC[_]](implicit a: Alt[TC], a0: TC[A1#Cop[Id]], a1: TC[A2#Cop[Id]], a2: TC[A3#Cop[Id]], a3: TC[A4#Cop[Id]], a4: TC[A5#Cop[Id]], a5: TC[A6#Cop[Id]], a6: TC[A7#Cop[Id]], a7: TC[A8#Cop[Id]], a8: TC[A9#Cop[Id]], dummy: DummyImplicit): TC[Cop[Id]] = mkAlt[TC, Cop[Id]](identity)
  def divide[TC[_]](implicit d: Divide[TC], a0: TC[A1#Prod[Id]], a1: TC[A2#Prod[Id]], a2: TC[A3#Prod[Id]], a3: TC[A4#Prod[Id]], a4: TC[A5#Prod[Id]], a5: TC[A6#Prod[Id]], a6: TC[A7#Prod[Id]], a7: TC[A8#Prod[Id]], a8: TC[A9#Prod[Id]], dummy: DummyImplicit): TC[Prod[Id]] = mkDivide[TC, Prod[Id]](identity)
  def apply[TC[_]](implicit a: Apply[TC], a0: TC[A1#Prod[Id]], a1: TC[A2#Prod[Id]], a2: TC[A3#Prod[Id]], a3: TC[A4#Prod[Id]], a4: TC[A5#Prod[Id]], a5: TC[A6#Prod[Id]], a6: TC[A7#Prod[Id]], a7: TC[A8#Prod[Id]], a8: TC[A9#Prod[Id]], dummy: DummyImplicit): TC[Prod[Id]] = mkApply[TC, Prod[Id]](identity)

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit def injEv[F[_]]: Inj[Cop[F], Cop[F]] = choose[Inj[Cop[F], ?], F]
    implicit def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = divide[Inj[Prod[F], ?], F]
  }
}

object AndXor9 {
  def apply[A1 <: AndXor, A2 <: AndXor, A3 <: AndXor, A4 <: AndXor, A5 <: AndXor, A6 <: AndXor, A7 <: AndXor, A8 <: AndXor, A9 <: AndXor]: AndXor9[A1, A2, A3, A4, A5, A6, A7, A8, A9] =
    new AndXor9[A1, A2, A3, A4, A5, A6, A7, A8, A9] {}
}
