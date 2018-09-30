package ldr
import scala.language.higherKinds
import scalaz.{Apply, Monoid, \/}
import scalaz.Id.Id
import scalaz.syntax.either._

trait LDRK6[F[_], A1, A2, A3, A4, A5, A6] extends LDR {
  type Prod = (F[A1], F[A2], F[A3], F[A4], F[A5], F[A6])
  type Cop = (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ F[A6])))))
  def combine[G[_]](implicit a0: G[F[A1]], a1: G[F[A2]], a2: G[F[A3]], a3: G[F[A4]], a4: G[F[A5]], a5: G[F[A6]]): ComposeLDR[G, Cop, Prod] =
    new ComposeLDR[G, Cop, Prod] {
      def mkChoose[B](f: B => Cop)(implicit d: Decidable[G]): G[B] =
        Combine.choose6(a0, a1, a2, a3, a4, a5)(f)
      def mkAlt[B](f: Cop => B)(implicit a: Alt[G]): G[B] =
        Combine.altly6(a0, a1, a2, a3, a4, a5)(f)
      def mkDivide[B](f: B => Prod)(implicit d: Divide[G]): G[B] =
        Combine.divide6(a0, a1, a2, a3, a4, a5)(f)
      def mkApply[B](f: Prod => B)(implicit a: Apply[G]): G[B] =
        Combine.apply6(a0, a1, a2, a3, a4, a5)((i0, i1, i2, i3, i4, i5) => f((i0, i1, i2, i3, i4, i5)))
    }

  object instances {

    implicit val inja0: Inj[Cop, F[A1]] =
      Inj.instance(_.left[(F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ F[A6]))))])

    implicit val inja1: Inj[Cop, F[A2]] =
      Inj.instance(_.left[(F[A3] \/ (F[A4] \/ (F[A5] \/ F[A6])))].right[F[A1]])

    implicit val inja2: Inj[Cop, F[A3]] =
      Inj.instance(_.left[(F[A4] \/ (F[A5] \/ F[A6]))].right[F[A2]].right[F[A1]])

    implicit val inja3: Inj[Cop, F[A4]] =
      Inj.instance(_.left[(F[A5] \/ F[A6])].right[F[A3]].right[F[A2]].right[F[A1]])

    implicit val inja4: Inj[Cop, F[A5]] =
      Inj.instance(_.left[F[A6]].right[F[A4]].right[F[A3]].right[F[A2]].right[F[A1]])

    implicit val inja5: Inj[Cop, F[A6]] =
      Inj.instance(_.right[F[A5]].right[F[A4]].right[F[A3]].right[F[A2]].right[F[A1]])

    implicit def lifta0(implicit M: Monoid[Prod]): Inj[Prod, F[A1]] = {
      val (_, a0, a1, a2, a3, a4) =
        M.zero
      Inj.instance((_, a0, a1, a2, a3, a4))
    }

    implicit def lifta1(implicit M: Monoid[Prod]): Inj[Prod, F[A2]] = {
      val (a0, _, a1, a2, a3, a4) =
        M.zero
      Inj.instance((a0, _, a1, a2, a3, a4))
    }

    implicit def lifta2(implicit M: Monoid[Prod]): Inj[Prod, F[A3]] = {
      val (a0, a1, _, a2, a3, a4) =
        M.zero
      Inj.instance((a0, a1, _, a2, a3, a4))
    }

    implicit def lifta3(implicit M: Monoid[Prod]): Inj[Prod, F[A4]] = {
      val (a0, a1, a2, _, a3, a4) =
        M.zero
      Inj.instance((a0, a1, a2, _, a3, a4))
    }

    implicit def lifta4(implicit M: Monoid[Prod]): Inj[Prod, F[A5]] = {
      val (a0, a1, a2, a3, _, a4) =
        M.zero
      Inj.instance((a0, a1, a2, a3, _, a4))
    }

    implicit def lifta5(implicit M: Monoid[Prod]): Inj[Prod, F[A6]] = {
      val (a0, a1, a2, a3, a4, _) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, _))
    }

  }

  import instances._

  val injEv = combine[Inj.Aux[Cop]#Out].choose
  def liftEv(implicit M: Monoid[Prod]): Inj[Prod, Prod] = combine[Inj.Aux[Prod]#Out].divide

}

object LDRK6 {

  def apply[F[_], A1, A2, A3, A4, A5, A6]: LDRK6[F, A1, A2, A3, A4, A5, A6] =
    new LDRK6[F, A1, A2, A3, A4, A5, A6] {}
}

trait LDRF6[A1, A2, A3, A4, A5, A6] {
  type Repr[F[_]] = LDRK6[F, A1, A2, A3, A4, A5, A6]
  def apply[F[_]]: Repr[F] =
    new LDRK6[F, A1, A2, A3, A4, A5, A6] {}
}

object LDRF6 {
  def apply[A1, A2, A3, A4, A5, A6]: LDRF6[A1, A2, A3, A4, A5, A6] =
    new LDRF6[A1, A2, A3, A4, A5, A6] {}
}

trait LDR6[A1, A2, A3, A4, A5, A6] extends LDRK6[Id, A1, A2, A3, A4, A5, A6]

object LDR6 {
  def apply[A1, A2, A3, A4, A5, A6]: LDR6[A1, A2, A3, A4, A5, A6] =
    new LDR6[A1, A2, A3, A4, A5, A6] {}
}
