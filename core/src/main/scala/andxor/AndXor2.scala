package andxor

import andxor.tuple._
import andxor.types._
import scalaz.{Apply, Functor, PlusEmpty, Monoid, \/, -\/, \/-, ~>}
import scalaz.Id.Id

trait AndXor2[A1, A2] extends AndXor {
  type Prod[F[_]] = Prod2[F, A1, A2]
  object Prod { def apply[F[_]](p: (F[A1], F[A2])): Prod[F] = Prod2(p) }

  type Cop[F[_]] = Cop2[F, A1, A2]
  object Cop { def apply[F[_]](c: (F[A1] \/ F[A2])): Cop[F] = Cop2(c) }

  def combine[F[_], G[_]](implicit a0: G[F[A1]], a1: G[F[A2]]): ComposeAndXor[F, G, Cop, Prod] =
    new ComposeAndXor[F, G, Cop, Prod] {
      def mkChoose[B](f: B => Cop[F])(implicit d: Decidable[G]): G[B] =
        Combine.choose2(a0, a1)(f(_).run)

      def mkAlt[B](f: Cop[F] => B)(implicit a: Alt[G]): G[B] =
        Combine.altly2(a0, a1)(x => f(Cop(x)))

      def mkDivide[B](f: B => Prod[F])(implicit d: Divide[G]): G[B] =
        Combine.divide2(a0, a1)(f(_).run)

      def mkApply[B](f: Prod[F] => B)(implicit a: Apply[G]): G[B] =
        Combine.apply2(a0, a1) {
          case (i0, i1) =>
            f(Prod((i0, i1)))
        }
    }

  def injEv[F[_]] = combine[F, Inj.Aux[Cop[F]]#Out].choose
  def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = combine[F, Inj.Aux[Prod[F]]#Out].divide

  def transformP[F[_], G[_]](nt: (F ~> G)): AndXor2[A1, A2]#Prod[F] => AndXor2[A1, A2]#Prod[G] =
    (p: AndXor2[A1, A2]#Prod[F]) => {
      val pr = p.run
      Prod[G]((nt(pr.t1), nt(pr.t2)))
    }

  def transformC[F[_], G[_]](nt: (F ~> G)): AndXor2[A1, A2]#Cop[F] => AndXor2[A1, A2]#Cop[G] =
    (p: AndXor2[A1, A2]#Cop[F]) => Cop[G](p.run.bimap(nt(_), nt(_)))

  def subst1[B]: AndXor2[B, A2] = AndXor2[B, A2]

  def subst2[B]: AndXor2[A1, B] = AndXor2[A1, B]

  // format: off
  def sequenceP[F[_]](prod: Prod[F])(implicit A: Apply[F]): F[Prod[Id]] = {
    val p = prod.run
    A.map(
    A.ap(p.t2)(
    A.map(p.t1)((i0: A1) => (i1: A2) =>
      (i0, i1))))(Prod[Id](_))
  }

  def sequenceC[F[_]](cop: Cop[F])(implicit FF: Functor[F]): F[Cop[Id]] =
    cop.run match {
      case -\/(x) => FF.map(FF.map(x)(y => -\/(y)))(Cop[Id](_))
      case \/-(x) => FF.map(FF.map(x)(y => \/-(y)))(Cop[Id](_))
    }

  def extractC[F[_], B](c: Cop[F])(implicit inj: Inj[Option[B], Cop[F]]): Option[B] = inj(c)

  def extractP[F[_], B](p: Prod[F])(implicit inj: Inj[B, Prod[F]]): B = inj(p)

  def foldMap[F[_], C](p: Prod[F])(map: Cop[F] => C)(implicit M: Monoid[C]): C = {
    val pr = p.run
    M.append(map(inj(pr.t1)), map(inj(pr.t2)))
  }

  def foldMapId[F[_], C](p: Prod[F])(map: Cop[Id] => C)(
      implicit O: Ordering[Cop[Id]], M: Monoid[C], PE: PlusEmpty[F], U: Uncons[F]): C = {
    import scala.collection.mutable.{PriorityQueue => PQ}
    def uncons(p: Prod[F]): (List[Cop[Id]], Prod[F]) = {
      val pr = p.run
      val ht1 = U(pr.t1)
      val ht2 = U(pr.t2)
      (List(ht1._1.map(inj(_: Id[A1])), ht2._1.map(inj(_: Id[A2]))).flatten,
        Prod[F]((ht1._2, ht2._2)))
    }
    @scala.annotation.tailrec
    def go(prod: Prod[F], q: PQ[Cop[Id]], out: C): C =
      (prod.run.==((PE.empty[A1], PE.empty[A2]))) match {
        case true =>
          q.foldLeft(out)((acc, el) => M.append(acc, map(el)))
        case false => q.isEmpty match {
          case true => {
            val (hs, ts) = uncons(prod)
            q ++= hs
            go(ts, q, out)
          }
          case false => q.dequeue.run match {
            case -\/(x) => {
              val pr = prod.run
              val (h, t) = U(pr.t1)
              go(Prod((t, pr.t2)),
                q ++= h.map(inj(_: Id[A1])), M.append(out, map(inj(x))))
          }
          case \/-(x) => {
              val pr = prod.run
              val (h, t) = U(pr.t2)
              go(Prod((pr.t1, t)),
                q ++= h.map(inj(_: Id[A2])), M.append(out, map(inj(x))))
          }

          }
        }
      }
    val Q = new PQ[Cop[Id]]()(O)
    val (hs, ts) = uncons(p)
    Q ++= hs
    go(ts, Q, M.zero)
  }
  // format: on
}

object AndXor2 {
  def apply[A1, A2]: AndXor2[A1, A2] =
    new AndXor2[A1, A2] {}
}
