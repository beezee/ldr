package andxor

import andxor.types.{Cop7, Prod7, TCDeps7}
import scala.annotation.tailrec
import scalaz.{Apply, Functor, PlusEmpty, Monoid, \/, -\/, \/-, ~>}
import scalaz.Id.Id
import scalaz.std.vector._

trait AndXor7[A1, A2, A3, A4, A5, A6, A7] extends AndXor {
  type Prod[F[_]] = Prod7[F, A1, A2, A3, A4, A5, A6, A7]
  object Prod { def apply[F[_]](p: (F[A1], F[A2], F[A3], F[A4], F[A5], F[A6], F[A7])): Prod[F] = Prod7[F, A1, A2, A3, A4, A5, A6, A7](p) }

  type Cop[F[_]] = Cop7[F, A1, A2, A3, A4, A5, A6, A7]
  object Cop { def apply[F[_]](c: (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ (F[A6] \/ F[A7]))))))): Cop[F] = Cop7[F, A1, A2, A3, A4, A5, A6, A7](c) }

  type TCDeps[TC[_], F[_]] = TCDeps7[TC, F, A1, A2, A3, A4, A5, A6, A7]

  def mkChoose[TC[_], F[_], B](f: B => Cop[F])(implicit d: Decidable[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.choose7(tcs.a0, tcs.a1, tcs.a2, tcs.a3, tcs.a4, tcs.a5, tcs.a6)(f(_).run)

  def mkAlt[TC[_], F[_], B](f: Cop[F] => B)(implicit a: Alt[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.altly7(tcs.a0, tcs.a1, tcs.a2, tcs.a3, tcs.a4, tcs.a5, tcs.a6)(x => f(Cop(x)))

  def mkDivide[TC[_], F[_], B](f: B => Prod[F])(implicit d: Divide[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.divide7(tcs.a0, tcs.a1, tcs.a2, tcs.a3, tcs.a4, tcs.a5, tcs.a6)(f(_).run)

  def mkApply[TC[_], F[_], B](f: Prod[F] => B)(implicit a: Apply[TC], tcs: TCDeps[TC, F]): TC[B] =
    Combine.apply7(tcs.a0, tcs.a1, tcs.a2, tcs.a3, tcs.a4, tcs.a5, tcs.a6) {
      case (i0, i1, i2, i3, i4, i5, i6) =>
        f(Prod((i0, i1, i2, i3, i4, i5, i6)))
    }

  object evidence extends AndXorEvidence[Cop, Prod] {
    implicit def injEv[F[_]]: Inj[Cop[F], Cop[F]] = choose[Inj[Cop[F], ?], F]
    implicit def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = divide[Inj[Prod[F], ?], F]
    implicit def injCopToProdEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Cop[F]] = choose[Inj[Prod[F], ?], F]
    implicit def injProdToVecCopEv[F[_]]: Inj[Vector[Cop[F]], Prod[F]] = divide[Inj[Vector[Cop[F]], ?], F]
  }

  def transformP[F[_], G[_]](nt: (F ~> G)): Prod[F] => Prod[G] =
    (p: Prod[F]) => Prod[G]((nt(p.t1), nt(p.t2), nt(p.t3), nt(p.t4), nt(p.t5), nt(p.t6), nt(p.t7)))

  def transformC[F[_], G[_]](nt: (F ~> G)): Cop[F] => Cop[G] =
    (c: Cop[F]) => Cop[G](c.run.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), nt(_))))))))

  // format: off
  def sequenceP[F[_]](p: Prod[F])(implicit A: Apply[F]): F[Prod[Id]] =
    A.map(
    A.ap(p.t7)(
    A.ap(p.t6)(
    A.ap(p.t5)(
    A.ap(p.t4)(
    A.ap(p.t3)(
    A.ap(p.t2)(
    A.map(p.t1)((i0: A1) => (i1: A2) => (i2: A3) => (i3: A4) => (i4: A5) => (i5: A6) => (i6: A7) =>
      (i0, i1, i2, i3, i4, i5, i6)))))))))(Prod[Id](_))

  def sequenceC[F[_]](cop: Cop[F])(implicit FF: Functor[F]): F[Cop[Id]] =
    cop.run match {
      case -\/(x) => FF.map(x)(y => Cop[Id](-\/(y)))
      case \/-(-\/(x)) => FF.map(x)(y => Cop[Id](\/-(-\/(y))))
      case \/-(\/-(-\/(x))) => FF.map(x)(y => Cop[Id](\/-(\/-(-\/(y)))))
      case \/-(\/-(\/-(-\/(x)))) => FF.map(x)(y => Cop[Id](\/-(\/-(\/-(-\/(y))))))
      case \/-(\/-(\/-(\/-(-\/(x))))) => FF.map(x)(y => Cop[Id](\/-(\/-(\/-(\/-(-\/(y)))))))
      case \/-(\/-(\/-(\/-(\/-(-\/(x)))))) => FF.map(x)(y => Cop[Id](\/-(\/-(\/-(\/-(\/-(-\/(y))))))))
      case \/-(\/-(\/-(\/-(\/-(\/-(x)))))) => FF.map(x)(y => Cop[Id](\/-(\/-(\/-(\/-(\/-(\/-(y))))))))
    }

  def extractC[F[_], B](c: Cop[F])(implicit inj: Inj[Option[B], Cop[F]]): Option[B] = inj(c)

  def extractP[F[_], B](p: Prod[F])(implicit inj: Inj[B, Prod[F]]): B = inj(p)

  def foldMap[G[_], C](p: Prod[G])(map: Cop[Id] => C)(implicit O: Ordering[Cop[Id]], M: Monoid[C], PE: PlusEmpty[G], U: Uncons[G]): C = {
    import scala.collection.mutable.{PriorityQueue => PQ}

    def uncons(p: Prod[G]): (List[Cop[Id]], Prod[G]) = {
      val ht1 = U(p.t1)
      val ht2 = U(p.t2)
      val ht3 = U(p.t3)
      val ht4 = U(p.t4)
      val ht5 = U(p.t5)
      val ht6 = U(p.t6)
      val ht7 = U(p.t7)
      (List(ht1._1.map(injId(_: A1)), ht2._1.map(injId(_: A2)), ht3._1.map(injId(_: A3)), ht4._1.map(injId(_: A4)), ht5._1.map(injId(_: A5)), ht6._1.map(injId(_: A6)), ht7._1.map(injId(_: A7))).flatten,
        Prod[G]((ht1._2, ht2._2, ht3._2, ht4._2, ht5._2, ht6._2, ht7._2)))
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
      (prod.run.==((PE.empty[A1], PE.empty[A2], PE.empty[A3], PE.empty[A4], PE.empty[A5], PE.empty[A6], PE.empty[A7]))) match {
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
              go(Prod[G]((t, prod.t2, prod.t3, prod.t4, prod.t5, prod.t6, prod.t7)),
                q ++= h.map(injId(_: A1)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(-\/(_)) =>
              val (h, t) = U(prod.t2)
              go(Prod[G]((prod.t1, t, prod.t3, prod.t4, prod.t5, prod.t6, prod.t7)),
                q ++= h.map(injId(_: A2)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(\/-(-\/(_))) =>
              val (h, t) = U(prod.t3)
              go(Prod[G]((prod.t1, prod.t2, t, prod.t4, prod.t5, prod.t6, prod.t7)),
                q ++= h.map(injId(_: A3)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(\/-(\/-(-\/(_)))) =>
              val (h, t) = U(prod.t4)
              go(Prod[G]((prod.t1, prod.t2, prod.t3, t, prod.t5, prod.t6, prod.t7)),
                q ++= h.map(injId(_: A4)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(\/-(\/-(\/-(-\/(_))))) =>
              val (h, t) = U(prod.t5)
              go(Prod[G]((prod.t1, prod.t2, prod.t3, prod.t4, t, prod.t6, prod.t7)),
                q ++= h.map(injId(_: A5)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(\/-(\/-(\/-(\/-(-\/(_)))))) =>
              val (h, t) = U(prod.t6)
              go(Prod[G]((prod.t1, prod.t2, prod.t3, prod.t4, prod.t5, t, prod.t7)),
                q ++= h.map(injId(_: A6)), M.append(out, map(Cop[Id](dj))))
            case dj @ \/-(\/-(\/-(\/-(\/-(\/-(_)))))) =>
              val (h, t) = U(prod.t7)
              go(Prod[G]((prod.t1, prod.t2, prod.t3, prod.t4, prod.t5, prod.t6, t)),
                q ++= h.map(injId(_: A7)), M.append(out, map(Cop[Id](dj))))

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

object AndXor7 {
  def apply[A1, A2, A3, A4, A5, A6, A7]: AndXor7[A1, A2, A3, A4, A5, A6, A7] =
    new AndXor7[A1, A2, A3, A4, A5, A6, A7] {}
}
