package ldr
import scala.language.higherKinds
import scalaz.{Apply, Monoid, \/, ~>}
import scalaz.Id.Id
import scalaz.syntax.either._

trait LDRK5[F[_], A1, A2, A3, A4, A5] extends LDR {
  type Prod = (F[A1], F[A2], F[A3], F[A4], F[A5])
  type Cop = (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ F[A5]))))
  def combine[G[_]](implicit a0: G[F[A1]], a1: G[F[A2]], a2: G[F[A3]], a3: G[F[A4]], a4: G[F[A5]]): ComposeLDR[G, Cop, Prod] =
    new ComposeLDR[G, Cop, Prod] {
      def mkChoose[B](f: B => Cop)(implicit d: Decidable[G]): G[B] =
        Combine.choose5(a0, a1, a2, a3, a4)(f)
      def mkAlt[B](f: Cop => B)(implicit a: Alt[G]): G[B] =
        Combine.altly5(a0, a1, a2, a3, a4)(f)
      def mkDivide[B](f: B => Prod)(implicit d: Divide[G]): G[B] =
        Combine.divide5(a0, a1, a2, a3, a4)(f)
      def mkApply[B](f: Prod => B)(implicit a: Apply[G]): G[B] =
        Combine.apply5(a0, a1, a2, a3, a4)((i0, i1, i2, i3, i4) => f((i0, i1, i2, i3, i4)))
    }

  import LDRK5._

  val injEv = combine[Inj.Aux[Cop]#Out].choose
  def liftEv(implicit M: Monoid[Prod]): Inj[Prod, Prod] = combine[Inj.Aux[Prod]#Out].divide

  def transformP[G[_]](nt: (F ~> G)): LDRK5[F, A1, A2, A3, A4, A5]#Prod => LDRK5[G, A1, A2, A3, A4, A5]#Prod =
    (p: LDRK5[F, A1, A2, A3, A4, A5]#Prod) => (nt(p._1), nt(p._2), nt(p._3), nt(p._4), nt(p._5))

  def transformC[G[_]](nt: (F ~> G)): LDRK5[F, A1, A2, A3, A4, A5]#Cop => LDRK5[G, A1, A2, A3, A4, A5]#Cop =
    (p: LDRK5[F, A1, A2, A3, A4, A5]#Cop) => p.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), nt(_)))))
}

object LDRK5 {

  def apply[F[_], A1, A2, A3, A4, A5]: LDRK5[F, A1, A2, A3, A4, A5] =
    new LDRK5[F, A1, A2, A3, A4, A5] {}

  implicit def inja0[F[_], A1, A2, A3, A4, A5]: Inj[LDRK5[F, A1, A2, A3, A4, A5]#Cop, F[A1]] =
    Inj.instance(_.left[(F[A2] \/ (F[A3] \/ (F[A4] \/ F[A5])))])

  implicit def inja1[F[_], A1, A2, A3, A4, A5]: Inj[LDRK5[F, A1, A2, A3, A4, A5]#Cop, F[A2]] =
    Inj.instance(_.left[(F[A3] \/ (F[A4] \/ F[A5]))].right[F[A1]])

  implicit def inja2[F[_], A1, A2, A3, A4, A5]: Inj[LDRK5[F, A1, A2, A3, A4, A5]#Cop, F[A3]] =
    Inj.instance(_.left[(F[A4] \/ F[A5])].right[F[A2]].right[F[A1]])

  implicit def inja3[F[_], A1, A2, A3, A4, A5]: Inj[LDRK5[F, A1, A2, A3, A4, A5]#Cop, F[A4]] =
    Inj.instance(_.left[F[A5]].right[F[A3]].right[F[A2]].right[F[A1]])

  implicit def inja4[F[_], A1, A2, A3, A4, A5]: Inj[LDRK5[F, A1, A2, A3, A4, A5]#Cop, F[A5]] =
    Inj.instance(_.right[F[A4]].right[F[A3]].right[F[A2]].right[F[A1]])

  implicit def lifta0[F[_], A1, A2, A3, A4, A5](implicit M: Monoid[LDRK5[F, A1, A2, A3, A4, A5]#Prod]): Inj[LDRK5[F, A1, A2, A3, A4, A5]#Prod, F[A1]] = {
    val (_, a0, a1, a2, a3) =
      M.zero
    Inj.instance((_, a0, a1, a2, a3))
  }

  implicit def lifta1[F[_], A1, A2, A3, A4, A5](implicit M: Monoid[LDRK5[F, A1, A2, A3, A4, A5]#Prod]): Inj[LDRK5[F, A1, A2, A3, A4, A5]#Prod, F[A2]] = {
    val (a0, _, a1, a2, a3) =
      M.zero
    Inj.instance((a0, _, a1, a2, a3))
  }

  implicit def lifta2[F[_], A1, A2, A3, A4, A5](implicit M: Monoid[LDRK5[F, A1, A2, A3, A4, A5]#Prod]): Inj[LDRK5[F, A1, A2, A3, A4, A5]#Prod, F[A3]] = {
    val (a0, a1, _, a2, a3) =
      M.zero
    Inj.instance((a0, a1, _, a2, a3))
  }

  implicit def lifta3[F[_], A1, A2, A3, A4, A5](implicit M: Monoid[LDRK5[F, A1, A2, A3, A4, A5]#Prod]): Inj[LDRK5[F, A1, A2, A3, A4, A5]#Prod, F[A4]] = {
    val (a0, a1, a2, _, a3) =
      M.zero
    Inj.instance((a0, a1, a2, _, a3))
  }

  implicit def lifta4[F[_], A1, A2, A3, A4, A5](implicit M: Monoid[LDRK5[F, A1, A2, A3, A4, A5]#Prod]): Inj[LDRK5[F, A1, A2, A3, A4, A5]#Prod, F[A5]] = {
    val (a0, a1, a2, a3, _) =
      M.zero
    Inj.instance((a0, a1, a2, a3, _))
  }

}

trait LDRF5[A1, A2, A3, A4, A5] {
  type Repr[F[_]] = LDRK5[F, A1, A2, A3, A4, A5]
  def apply[F[_]]: Repr[F] =
    new LDRK5[F, A1, A2, A3, A4, A5] {}
}

object LDRF5 {
  def apply[A1, A2, A3, A4, A5]: LDRF5[A1, A2, A3, A4, A5] =
    new LDRF5[A1, A2, A3, A4, A5] {}
}

trait LDR5[A1, A2, A3, A4, A5] extends LDRK5[Id, A1, A2, A3, A4, A5]

object LDR5 {
  def apply[A1, A2, A3, A4, A5]: LDR5[A1, A2, A3, A4, A5] =
    new LDR5[A1, A2, A3, A4, A5] {}
}