package andxor

import andxor.types.{Cop2, Cop2New, CopTCDeps2, Prod2, Prod25, TCDeps2, ProdTCDeps2}
import scala.annotation.tailrec
import scalaz.{Apply, Functor, PlusEmpty, Monoid, \/, -\/, \/-, ~>}
import scalaz.Id.Id
import scalaz.std.vector._

trait AndXor2New[A1 <: AndXor, A2 <: AndXor] extends AndXorNew {
  type Prod[F[_]] = Prod25[F, A1, A2]
  object Prod { def apply[F[_]](p: (A1#Prod[F], A2#Prod[F])): Prod[F] = Prod25[F, A1, A2](p) }

  type Cop[F[_]] = Cop2New[F, A1, A2]
  object Cop { def apply[F[_]](c: (A1#Cop[F] \/ A2#Cop[F])): Cop[F] = Cop2New[F, A1, A2](c) }

  type ProdTCDeps[TC[_], F[_]] = ProdTCDeps2[TC, F, A1, A2]
  type CopTCDeps[TC[_], F[_]] = CopTCDeps2[TC, F, A1, A2]

  def mkChoose[TC[_], F[_], B](f: B => Cop[F])(implicit d: Decidable[TC], tcs: CopTCDeps[TC, F]): TC[B] =
    Combine.choose2(tcs.a0, tcs.a1)(f(_).run)

  def mkAlt[TC[_], F[_], B](f: Cop[F] => B)(implicit a: Alt[TC], tcs: CopTCDeps[TC, F]): TC[B] =
    Combine.altly2(tcs.a0, tcs.a1)(x => f(Cop(x)))

  def mkDivide[TC[_], F[_], B](f: B => Prod[F])(implicit d: Divide[TC], tcs: ProdTCDeps[TC, F]): TC[B] =
    Combine.divide2(tcs.a0, tcs.a1)(f(_).run)

  def mkApply[TC[_], F[_], B](f: Prod[F] => B)(implicit a: Apply[TC], tcs: ProdTCDeps[TC, F]): TC[B] =
    Combine.apply2(tcs.a0, tcs.a1) {
      case (i0, i1) =>
        f(Prod((i0, i1)))
    }

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit def injEv[F[_]]: Inj[Cop[F], Cop[F]] = choose[Inj[Cop[F], ?], F]
    implicit def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = divide[Inj[Prod[F], ?], F]
    implicit def injCopToProdEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Cop[F]] = choose[Inj[Prod[F], ?], F]
    implicit def injProdToVecCopEv[F[_]]: Inj[Vector[Cop[F]], Prod[F]] = divide[Inj[Vector[Cop[F]], ?], F]
  }
}

trait AndXor2[A1, A2] extends AndXor {
  type Prod[F[_]] = Prod2[F, A1, A2]
  object Prod { def apply[F[_]](p: (F[A1], F[A2])): Prod[F] = Prod2[F, A1, A2](p) }

  type Cop[F[_]] = Cop2[F, A1, A2]
  object Cop { def apply[F[_]](c: (F[A1] \/ F[A2])): Cop[F] = Cop2[F, A1, A2](c) }

  type TCDeps[TC[_], F[_]] = TCDeps2[TC, F, A1, A2]

  def mkChoose[TC[_], F[_], B](f: B => Cop[F])(implicit d: Decidable[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.choose2(tcs.a0, tcs.a1)(f(_).run)

  def mkAlt[TC[_], F[_], B](f: Cop[F] => B)(implicit a: Alt[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.altly2(tcs.a0, tcs.a1)(x => f(Cop(x)))

  def mkDivide[TC[_], F[_], B](f: B => Prod[F])(implicit d: Divide[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.divide2(tcs.a0, tcs.a1)(f(_).run)

  def mkApply[TC[_], F[_], B](f: Prod[F] => B)(implicit a: Apply[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.apply2(tcs.a0, tcs.a1) {
      case (i0, i1) =>
        f(Prod((i0, i1)))
    }

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit def injEv[F[_]]: Inj[Cop[F], Cop[F]] = choose[Inj[Cop[F], ?], F]
    implicit def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = divide[Inj[Prod[F], ?], F]
    implicit def injCopToProdEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Cop[F]] = choose[Inj[Prod[F], ?], F]
    implicit def injProdToVecCopEv[F[_]]: Inj[Vector[Cop[F]], Prod[F]] = divide[Inj[Vector[Cop[F]], ?], F]
  }

  def transformP[F[_], G[_]](nt: (F ~> G)): Prod[F] => Prod[G] =
    (p: Prod[F]) => Prod[G]((nt(p.t1), nt(p.t2)))

  def transformC[F[_], G[_]](nt: (F ~> G)): Cop[F] => Cop[G] =
    (c: Cop[F]) => Cop[G](c.run.bimap(nt(_), nt(_)))

  // format: off
  def sequenceP[F[_]](p: Prod[F])(implicit A: Apply[F]): F[Prod[Id]] =
    A.map(
    A.ap(p.t2)(
    A.map(p.t1)((i0: A1) => (i1: A2) =>
      (i0, i1))))(Prod[Id](_))

  def sequenceC[F[_]](cop: Cop[F])(implicit FF: Functor[F]): F[Cop[Id]] =
    cop.run match {
      case -\/(x) => FF.map(x)(y => Cop[Id](-\/(y)))
      case \/-(x) => FF.map(x)(y => Cop[Id](\/-(y)))
    }

  def extractC[F[_], B](c: Cop[F])(implicit inj: Inj[Option[B], Cop[F]]): Option[B] = inj(c)

  def extractP[F[_], B](p: Prod[F])(implicit inj: Inj[B, Prod[F]]): B = inj(p)

  def foldMap[G[_], C](p: Prod[G])(map: Cop[Id] => C)(implicit O: Ordering[Cop[Id]], M: Monoid[C], PE: PlusEmpty[G], U: Uncons[G]): C = {
    import scala.collection.mutable.{PriorityQueue => PQ}

    def uncons(p: Prod[G]): (List[Cop[Id]], Prod[G]) = {
      val ht1 = U(p.t1)
      val ht2 = U(p.t2)
      (List(ht1._1.map(injId(_: A1)), ht2._1.map(injId(_: A2))).flatten,
        Prod[G]((ht1._2, ht2._2)))
    }

    @tailrec
    def appendAll(out: C, q: PQ[Cop[Id]]): C =
      q.isEmpty match {
        case true => out
        case false =>
          val newOut = M.append(out, map(q.dequeue))
          appendAll(newOut, q)
      }

    @tailrec
    def go(prod: Prod[G], q: PQ[Cop[Id]], out: C): C =
      (prod.run.==((PE.empty[A1], PE.empty[A2]))) match {
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
              go(Prod[G]((t, prod.t2)),
                q ++= h.map(injId(_: A1)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(_) =>
              val (h, t) = U(prod.t2)
              go(Prod[G]((prod.t1, t)),
                q ++= h.map(injId(_: A2)), M.append(out, map(Cop[Id](dj))))

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
