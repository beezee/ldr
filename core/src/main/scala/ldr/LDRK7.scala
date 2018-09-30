package ldr
import scala.language.higherKinds
import scalaz.{Apply, Monoid, \/, ~>}
import scalaz.Id.Id
import scalaz.syntax.either._

trait LDRK7[F[_], A1, A2, A3, A4, A5, A6, A7] extends LDR {
  type Prod = (F[A1], F[A2], F[A3], F[A4], F[A5], F[A6], F[A7])
  type Cop = (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ (F[A6] \/ F[A7]))))))
  def combine[G[_]](implicit a0: G[F[A1]], a1: G[F[A2]], a2: G[F[A3]], a3: G[F[A4]], a4: G[F[A5]], a5: G[F[A6]], a6: G[F[A7]]): ComposeLDR[G, Cop, Prod] =
    new ComposeLDR[G, Cop, Prod] {
      def mkChoose[B](f: B => Cop)(implicit d: Decidable[G]): G[B] =
        Combine.choose7(a0, a1, a2, a3, a4, a5, a6)(f)
      def mkAlt[B](f: Cop => B)(implicit a: Alt[G]): G[B] =
        Combine.altly7(a0, a1, a2, a3, a4, a5, a6)(f)
      def mkDivide[B](f: B => Prod)(implicit d: Divide[G]): G[B] =
        Combine.divide7(a0, a1, a2, a3, a4, a5, a6)(f)
      def mkApply[B](f: Prod => B)(implicit a: Apply[G]): G[B] =
        Combine.apply7(a0, a1, a2, a3, a4, a5, a6)((i0, i1, i2, i3, i4, i5, i6) => f((i0, i1, i2, i3, i4, i5, i6)))
    }

  import LDRK7._

  val injEv = combine[Inj.Aux[Cop]#Out].choose
  def liftEv(implicit M: Monoid[Prod]): Inj[Prod, Prod] = combine[Inj.Aux[Prod]#Out].divide

  def transformP[G[_]](nt: (F ~> G)): LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod => LDRK7[G, A1, A2, A3, A4, A5, A6, A7]#Prod =
    (p: LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod) => (nt(p._1), nt(p._2), nt(p._3), nt(p._4), nt(p._5), nt(p._6), nt(p._7))

  def transformC[G[_]](nt: (F ~> G)): LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop => LDRK7[G, A1, A2, A3, A4, A5, A6, A7]#Cop =
    (p: LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop) => p.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), nt(_)))))))
}

object LDRK7 {

  def apply[F[_], A1, A2, A3, A4, A5, A6, A7]: LDRK7[F, A1, A2, A3, A4, A5, A6, A7] =
    new LDRK7[F, A1, A2, A3, A4, A5, A6, A7] {}

  implicit def inja0[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A1]] =
    Inj.instance(_.left[(F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ (F[A6] \/ F[A7])))))])

  implicit def inja1[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A2]] =
    Inj.instance(_.left[(F[A3] \/ (F[A4] \/ (F[A5] \/ (F[A6] \/ F[A7]))))].right[F[A1]])

  implicit def inja2[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A3]] =
    Inj.instance(_.left[(F[A4] \/ (F[A5] \/ (F[A6] \/ F[A7])))].right[F[A2]].right[F[A1]])

  implicit def inja3[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A4]] =
    Inj.instance(_.left[(F[A5] \/ (F[A6] \/ F[A7]))].right[F[A3]].right[F[A2]].right[F[A1]])

  implicit def inja4[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A5]] =
    Inj.instance(_.left[(F[A6] \/ F[A7])].right[F[A4]].right[F[A3]].right[F[A2]].right[F[A1]])

  implicit def inja5[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A6]] =
    Inj.instance(_.left[F[A7]].right[F[A5]].right[F[A4]].right[F[A3]].right[F[A2]].right[F[A1]])

  implicit def inja6[F[_], A1, A2, A3, A4, A5, A6, A7]: Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Cop, F[A7]] =
    Inj.instance(_.right[F[A6]].right[F[A5]].right[F[A4]].right[F[A3]].right[F[A2]].right[F[A1]])

  implicit def lifta0[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A1]] = {
    val (_, a0, a1, a2, a3, a4, a5) =
      M.zero
    Inj.instance((_, a0, a1, a2, a3, a4, a5))
  }

  implicit def lifta1[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A2]] = {
    val (a0, _, a1, a2, a3, a4, a5) =
      M.zero
    Inj.instance((a0, _, a1, a2, a3, a4, a5))
  }

  implicit def lifta2[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A3]] = {
    val (a0, a1, _, a2, a3, a4, a5) =
      M.zero
    Inj.instance((a0, a1, _, a2, a3, a4, a5))
  }

  implicit def lifta3[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A4]] = {
    val (a0, a1, a2, _, a3, a4, a5) =
      M.zero
    Inj.instance((a0, a1, a2, _, a3, a4, a5))
  }

  implicit def lifta4[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A5]] = {
    val (a0, a1, a2, a3, _, a4, a5) =
      M.zero
    Inj.instance((a0, a1, a2, a3, _, a4, a5))
  }

  implicit def lifta5[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A6]] = {
    val (a0, a1, a2, a3, a4, _, a5) =
      M.zero
    Inj.instance((a0, a1, a2, a3, a4, _, a5))
  }

  implicit def lifta6[F[_], A1, A2, A3, A4, A5, A6, A7](implicit M: Monoid[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod]): Inj[LDRK7[F, A1, A2, A3, A4, A5, A6, A7]#Prod, F[A7]] = {
    val (a0, a1, a2, a3, a4, a5, _) =
      M.zero
    Inj.instance((a0, a1, a2, a3, a4, a5, _))
  }

}

trait LDRF7[A1, A2, A3, A4, A5, A6, A7] {
  type Repr[F[_]] = LDRK7[F, A1, A2, A3, A4, A5, A6, A7]
  def apply[F[_]]: Repr[F] =
    new LDRK7[F, A1, A2, A3, A4, A5, A6, A7] {}
}

object LDRF7 {
  def apply[A1, A2, A3, A4, A5, A6, A7]: LDRF7[A1, A2, A3, A4, A5, A6, A7] =
    new LDRF7[A1, A2, A3, A4, A5, A6, A7] {}
}

trait LDR7[A1, A2, A3, A4, A5, A6, A7] extends LDRK7[Id, A1, A2, A3, A4, A5, A6, A7]

object LDR7 {
  def apply[A1, A2, A3, A4, A5, A6, A7]: LDR7[A1, A2, A3, A4, A5, A6, A7] =
    new LDR7[A1, A2, A3, A4, A5, A6, A7] {}
}