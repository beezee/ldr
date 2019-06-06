package andxor

import andxor.types.{Cop5, Prod5}
import scala.annotation.tailrec
import scalaz.{Apply, Functor, PlusEmpty, Monoid, \/, -\/, \/-, ~>}
import scalaz.Id.Id
import scalaz.std.vector._

trait AndXorK5[F[_], A1, A2, A3, A4, A5] extends AndXor {
  type Prod = Prod5[F, A1, A2, A3, A4, A5]
  object Prod { def apply(p: (F[A1], F[A2], F[A3], F[A4], F[A5])): Prod = Prod5[F, A1, A2, A3, A4, A5](p) }

  type Cop = Cop5[F, A1, A2, A3, A4, A5]
  object Cop { def apply(c: (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ F[A5]))))): Cop = Cop5[F, A1, A2, A3, A4, A5](c) }

  val AndXorF = AndXorF5[A1, A2, A3, A4, A5]
  type AndXor[G[_]] = AndXorF.Repr[G]

  def combineId[G[_]](
      implicit @scalaz.unused ev: F[_] =:= Id[_],
      a0: G[A1],
      a1: G[A2],
      a2: G[A3],
      a3: G[A4],
      a4: G[A5]
  ): ComposeAndXor[G, Cop5[Id, A1, A2, A3, A4, A5], Prod5[Id, A1, A2, A3, A4, A5]] =
    AndXor5[A1, A2, A3, A4, A5].combine[G]

  def combine[G[_]](implicit a0: G[F[A1]], a1: G[F[A2]], a2: G[F[A3]], a3: G[F[A4]], a4: G[F[A5]]): ComposeAndXor[G, Cop, Prod] =
    new ComposeAndXor[G, Cop, Prod] {
      def mkChoose[B](f: B => Cop)(implicit d: Decidable[G]): G[B] =
        Combine.choose5(a0, a1, a2, a3, a4)(f(_).run)

      def mkAlt[B](f: Cop => B)(implicit a: Alt[G]): G[B] =
        Combine.altly5(a0, a1, a2, a3, a4)(x => f(Cop(x)))

      def mkDivide[B](f: B => Prod)(implicit d: Divide[G]): G[B] =
        Combine.divide5(a0, a1, a2, a3, a4)(f(_).run)

      def mkApply[B](f: Prod => B)(implicit a: Apply[G]): G[B] =
        Combine.apply5(a0, a1, a2, a3, a4) {
          case (i0, i1, i2, i3, i4) =>
            f(Prod((i0, i1, i2, i3, i4)))
        }

    }

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit val injEv: Inj[Cop, Cop] = combine[Inj[Cop, ?]].choose
    implicit def liftEv(implicit M: Monoid[Prod]): Inj[Prod, Prod] = combine[Inj[Prod, ?]].divide
    implicit def injCopToProdEv(implicit M: Monoid[Prod]): Inj[Prod, Cop] = combine[Inj[Prod, ?]].choose
    implicit val injProdToVecCopEv: Inj[Vector[Cop], Prod] = combine[Inj[Vector[Cop], ?]].divide
  }

  def transformP[G[_]](nt: (F ~> G)): AndXorK5[F, A1, A2, A3, A4, A5]#Prod => AndXorK5[G, A1, A2, A3, A4, A5]#Prod =
    (p: AndXorK5[F, A1, A2, A3, A4, A5]#Prod) => Prod5[G, A1, A2, A3, A4, A5]((nt(p.t1), nt(p.t2), nt(p.t3), nt(p.t4), nt(p.t5)))

  def transformC[G[_]](nt: (F ~> G)): AndXorK5[F, A1, A2, A3, A4, A5]#Cop => AndXorK5[G, A1, A2, A3, A4, A5]#Cop =
    (p: AndXorK5[F, A1, A2, A3, A4, A5]#Cop) =>
      Cop5[G, A1, A2, A3, A4, A5](
        p.run.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), nt(_)))))
      )

  def subst1[G[_]]: AndXor5[G[A1], F[A2], F[A3], F[A4], F[A5]] = AndXor5[G[A1], F[A2], F[A3], F[A4], F[A5]]

  def subst2[G[_]]: AndXor5[F[A1], G[A2], F[A3], F[A4], F[A5]] = AndXor5[F[A1], G[A2], F[A3], F[A4], F[A5]]

  def subst3[G[_]]: AndXor5[F[A1], F[A2], G[A3], F[A4], F[A5]] = AndXor5[F[A1], F[A2], G[A3], F[A4], F[A5]]

  def subst4[G[_]]: AndXor5[F[A1], F[A2], F[A3], G[A4], F[A5]] = AndXor5[F[A1], F[A2], F[A3], G[A4], F[A5]]

  def subst5[G[_]]: AndXor5[F[A1], F[A2], F[A3], F[A4], G[A5]] = AndXor5[F[A1], F[A2], F[A3], F[A4], G[A5]]

  // format: off
  def sequenceP(p: Prod)(implicit A: Apply[F]): F[Prod5[Id, A1, A2, A3, A4, A5]] = {
    A.map(
    A.ap(p.t5)(
    A.ap(p.t4)(
    A.ap(p.t3)(
    A.ap(p.t2)(
    A.map(p.t1)((i0: A1) => (i1: A2) => (i2: A3) => (i3: A4) => (i4: A5) =>
      (i0, i1, i2, i3, i4)))))))(Prod5[Id, A1, A2, A3, A4, A5](_))
  }

  def sequenceC(cop: Cop)(implicit FF: Functor[F]): F[Cop5[Id, A1, A2, A3, A4, A5]] =
    cop.run match {
      case -\/(x) => FF.map(x)(y => Cop5[Id, A1, A2, A3, A4, A5](-\/(y)))
      case \/-(-\/(x)) => FF.map(x)(y => Cop5[Id, A1, A2, A3, A4, A5](\/-(-\/(y))))
      case \/-(\/-(-\/(x))) => FF.map(x)(y => Cop5[Id, A1, A2, A3, A4, A5](\/-(\/-(-\/(y)))))
      case \/-(\/-(\/-(-\/(x)))) => FF.map(x)(y => Cop5[Id, A1, A2, A3, A4, A5](\/-(\/-(\/-(-\/(y))))))
      case \/-(\/-(\/-(\/-(x)))) => FF.map(x)(y => Cop5[Id, A1, A2, A3, A4, A5](\/-(\/-(\/-(\/-(y))))))
    }

  def extractC[B](c: Cop)(implicit inj: Inj[Option[B], Cop]): Option[B] = inj(c)

  def extractP[B](p: Prod)(implicit inj: Inj[B, Prod]): B = inj(p)

  def foldMap[G[_], C](p: AndXor[G]#Prod)(map: AndXor[Id]#Cop => C)(
      implicit O: Ordering[Cop5[Id, A1, A2, A3, A4, A5]], M: Monoid[C], PE: PlusEmpty[G], U: Uncons[G]): C = {
    import scala.collection.mutable.{PriorityQueue => PQ}

    val TG = AndXorF[G]
    val TI = AndXorF[Id]

    def uncons(p: TG.Prod): (List[TI.Cop], TG.Prod) = {
      val ht1 = U(p.t1)
      val ht2 = U(p.t2)
      val ht3 = U(p.t3)
      val ht4 = U(p.t4)
      val ht5 = U(p.t5)
      (List(ht1._1.map(TI.inj(_: Id[A1])), ht2._1.map(TI.inj(_: Id[A2])), ht3._1.map(TI.inj(_: Id[A3])), ht4._1.map(TI.inj(_: Id[A4])), ht5._1.map(TI.inj(_: Id[A5]))).flatten,
        TG.Prod((ht1._2, ht2._2, ht3._2, ht4._2, ht5._2)))
    }

    @tailrec
    def appendAll(out: C, q: PQ[TI.Cop]): C =
      q.isEmpty match {
        case true => out
        case false =>
          val newOut = M.append(out, map(q.dequeue))
          appendAll(newOut, q)
      }

    @tailrec
    def go(prod: TG.Prod, q: PQ[TI.Cop], out: C): C =
      (prod.run.==((PE.empty[A1], PE.empty[A2], PE.empty[A3], PE.empty[A4], PE.empty[A5]))) match {
        case true => appendAll(out, q)
        case false => q.isEmpty match {
          case true => {
            val (hs, ts) = uncons(prod)
            q ++= hs
            go(ts, q, out)
          }
          case false => q.dequeue.run match {
            case dj @ -\/(_) =>
              val (h, t) = U(prod.t1)
              go(TG.Prod((t, prod.t2, prod.t3, prod.t4, prod.t5)),
                q ++= h.map(TI.inj(_: Id[A1])), M.append(out, map(TI.Cop(dj))))
            case dj @ \/-(-\/(_)) =>
              val (h, t) = U(prod.t2)
              go(TG.Prod((prod.t1, t, prod.t3, prod.t4, prod.t5)),
                q ++= h.map(TI.inj(_: Id[A2])), M.append(out, map(TI.Cop(dj))))
            case dj @ \/-(\/-(-\/(_))) =>
              val (h, t) = U(prod.t3)
              go(TG.Prod((prod.t1, prod.t2, t, prod.t4, prod.t5)),
                q ++= h.map(TI.inj(_: Id[A3])), M.append(out, map(TI.Cop(dj))))
            case dj @ \/-(\/-(\/-(-\/(_)))) =>
              val (h, t) = U(prod.t4)
              go(TG.Prod((prod.t1, prod.t2, prod.t3, t, prod.t5)),
                q ++= h.map(TI.inj(_: Id[A4])), M.append(out, map(TI.Cop(dj))))
            case dj @ \/-(\/-(\/-(\/-(_)))) =>
              val (h, t) = U(prod.t5)
              go(TG.Prod((prod.t1, prod.t2, prod.t3, prod.t4, t)),
                q ++= h.map(TI.inj(_: Id[A5])), M.append(out, map(TI.Cop(dj))))

          }
        }
      }
    val Q = new PQ[TI.Cop]()(O)
    val (hs, ts) = uncons(p)
    Q ++= hs
    go(ts, Q, M.zero)
  }
  // format: on
}

object AndXorK5 {

  def apply[F[_], A1, A2, A3, A4, A5]: AndXorK5[F, A1, A2, A3, A4, A5] =
    new AndXorK5[F, A1, A2, A3, A4, A5] {}
}

trait AndXorF5[A1, A2, A3, A4, A5] {
  type Repr[F[_]] = AndXorK5[F, A1, A2, A3, A4, A5]
  def apply[F[_]]: Repr[F] =
    new AndXorK5[F, A1, A2, A3, A4, A5] {}
}

object AndXorF5 {
  def apply[A1, A2, A3, A4, A5]: AndXorF5[A1, A2, A3, A4, A5] =
    new AndXorF5[A1, A2, A3, A4, A5] {}
}

trait AndXor5[A1, A2, A3, A4, A5] extends AndXorK5[Id, A1, A2, A3, A4, A5]

object AndXor5 {
  def apply[A1, A2, A3, A4, A5]: AndXor5[A1, A2, A3, A4, A5] =
    new AndXor5[A1, A2, A3, A4, A5] {}
}
