package andxor

import andxor.tuple._
import scala.language.higherKinds
import scalaz.{Apply, Functor, PlusEmpty, Monoid, \/, -\/, \/-, ~>}
import scalaz.Id.Id

trait AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] extends AndXor {
  case class ProdT[F[_]](run: (F[A1], (F[A2], (F[A3], (F[A4], (F[A5], (F[A6], (F[A7], (F[A8], (F[A9], F[A10]))))))))))
  object ProdT {

    implicit def lifta0[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A1]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((x, (t.t2, (t.t3, (t.t4, (t.t5, (t.t6, (t.t7, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta0Inverse[F[_]]: Inj[F[A1], ProdT[F]] = Inj.instance(_.run.t1)

    implicit def lifta1[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A2]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (x, (t.t3, (t.t4, (t.t5, (t.t6, (t.t7, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta1Inverse[F[_]]: Inj[F[A2], ProdT[F]] = Inj.instance(_.run.t2)

    implicit def lifta2[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A3]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (x, (t.t4, (t.t5, (t.t6, (t.t7, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta2Inverse[F[_]]: Inj[F[A3], ProdT[F]] = Inj.instance(_.run.t3)

    implicit def lifta3[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A4]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (x, (t.t5, (t.t6, (t.t7, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta3Inverse[F[_]]: Inj[F[A4], ProdT[F]] = Inj.instance(_.run.t4)

    implicit def lifta4[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A5]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (t.t4, (x, (t.t6, (t.t7, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta4Inverse[F[_]]: Inj[F[A5], ProdT[F]] = Inj.instance(_.run.t5)

    implicit def lifta5[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A6]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (t.t4, (t.t5, (x, (t.t7, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta5Inverse[F[_]]: Inj[F[A6], ProdT[F]] = Inj.instance(_.run.t6)

    implicit def lifta6[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A7]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (t.t4, (t.t5, (t.t6, (x, (t.t8, (t.t9, t.t10)))))))))))
    }

    implicit def lifta6Inverse[F[_]]: Inj[F[A7], ProdT[F]] = Inj.instance(_.run.t7)

    implicit def lifta7[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A8]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (t.t4, (t.t5, (t.t6, (t.t7, (x, (t.t9, t.t10)))))))))))
    }

    implicit def lifta7Inverse[F[_]]: Inj[F[A8], ProdT[F]] = Inj.instance(_.run.t8)

    implicit def lifta8[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A9]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (t.t4, (t.t5, (t.t6, (t.t7, (t.t8, (x, t.t10)))))))))))
    }

    implicit def lifta8Inverse[F[_]]: Inj[F[A9], ProdT[F]] = Inj.instance(_.run.t9)

    implicit def lifta9[F[_]](implicit M: Monoid[ProdT[F]]): Inj[ProdT[F], F[A10]] = {
      val t = M.zero.run
      Inj.instance(x => ProdT((t.t1, (t.t2, (t.t3, (t.t4, (t.t5, (t.t6, (t.t7, (t.t8, (t.t9, x)))))))))))
    }

    implicit def lifta9Inverse[F[_]]: Inj[F[A10], ProdT[F]] = Inj.instance(_.run.t10)

  }

  type Prod[F[_]] = ProdT[F]

  case class CopT[F[_]](run: (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ (F[A6] \/ (F[A7] \/ (F[A8] \/ (F[A9] \/ F[A10]))))))))))
  object CopT {

    implicit def prisma0[F[_]]: Prism[CopT[F], F[A1]] = new Prism[CopT[F], F[A1]] {
      def getOption(c: CopT[F]): Option[F[A1]] = c.run match {
        case -\/(x) => Some(x)
        case _      => None
      }
      def reverseGet(x: F[A1]): CopT[F] = CopT(-\/(x))
    }

    implicit def inja0[F[_]]: Inj[CopT[F], F[A1]] = Inj.instance(prisma0.reverseGet(_))
    implicit def inja0Inverse[F[_]]: Inj[Option[F[A1]], CopT[F]] = Inj.instance(prisma0.getOption(_))

    implicit def prisma1[F[_]]: Prism[CopT[F], F[A2]] = new Prism[CopT[F], F[A2]] {
      def getOption(c: CopT[F]): Option[F[A2]] = c.run match {
        case \/-(-\/(x)) => Some(x)
        case _           => None
      }
      def reverseGet(x: F[A2]): CopT[F] = CopT(\/-(-\/(x)))
    }

    implicit def inja1[F[_]]: Inj[CopT[F], F[A2]] = Inj.instance(prisma1.reverseGet(_))
    implicit def inja1Inverse[F[_]]: Inj[Option[F[A2]], CopT[F]] = Inj.instance(prisma1.getOption(_))

    implicit def prisma2[F[_]]: Prism[CopT[F], F[A3]] = new Prism[CopT[F], F[A3]] {
      def getOption(c: CopT[F]): Option[F[A3]] = c.run match {
        case \/-(\/-(-\/(x))) => Some(x)
        case _                => None
      }
      def reverseGet(x: F[A3]): CopT[F] = CopT(\/-(\/-(-\/(x))))
    }

    implicit def inja2[F[_]]: Inj[CopT[F], F[A3]] = Inj.instance(prisma2.reverseGet(_))
    implicit def inja2Inverse[F[_]]: Inj[Option[F[A3]], CopT[F]] = Inj.instance(prisma2.getOption(_))

    implicit def prisma3[F[_]]: Prism[CopT[F], F[A4]] = new Prism[CopT[F], F[A4]] {
      def getOption(c: CopT[F]): Option[F[A4]] = c.run match {
        case \/-(\/-(\/-(-\/(x)))) => Some(x)
        case _                     => None
      }
      def reverseGet(x: F[A4]): CopT[F] = CopT(\/-(\/-(\/-(-\/(x)))))
    }

    implicit def inja3[F[_]]: Inj[CopT[F], F[A4]] = Inj.instance(prisma3.reverseGet(_))
    implicit def inja3Inverse[F[_]]: Inj[Option[F[A4]], CopT[F]] = Inj.instance(prisma3.getOption(_))

    implicit def prisma4[F[_]]: Prism[CopT[F], F[A5]] = new Prism[CopT[F], F[A5]] {
      def getOption(c: CopT[F]): Option[F[A5]] = c.run match {
        case \/-(\/-(\/-(\/-(-\/(x))))) => Some(x)
        case _                          => None
      }
      def reverseGet(x: F[A5]): CopT[F] = CopT(\/-(\/-(\/-(\/-(-\/(x))))))
    }

    implicit def inja4[F[_]]: Inj[CopT[F], F[A5]] = Inj.instance(prisma4.reverseGet(_))
    implicit def inja4Inverse[F[_]]: Inj[Option[F[A5]], CopT[F]] = Inj.instance(prisma4.getOption(_))

    implicit def prisma5[F[_]]: Prism[CopT[F], F[A6]] = new Prism[CopT[F], F[A6]] {
      def getOption(c: CopT[F]): Option[F[A6]] = c.run match {
        case \/-(\/-(\/-(\/-(\/-(-\/(x)))))) => Some(x)
        case _                               => None
      }
      def reverseGet(x: F[A6]): CopT[F] = CopT(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))
    }

    implicit def inja5[F[_]]: Inj[CopT[F], F[A6]] = Inj.instance(prisma5.reverseGet(_))
    implicit def inja5Inverse[F[_]]: Inj[Option[F[A6]], CopT[F]] = Inj.instance(prisma5.getOption(_))

    implicit def prisma6[F[_]]: Prism[CopT[F], F[A7]] = new Prism[CopT[F], F[A7]] {
      def getOption(c: CopT[F]): Option[F[A7]] = c.run match {
        case \/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))) => Some(x)
        case _                                    => None
      }
      def reverseGet(x: F[A7]): CopT[F] = CopT(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))
    }

    implicit def inja6[F[_]]: Inj[CopT[F], F[A7]] = Inj.instance(prisma6.reverseGet(_))
    implicit def inja6Inverse[F[_]]: Inj[Option[F[A7]], CopT[F]] = Inj.instance(prisma6.getOption(_))

    implicit def prisma7[F[_]]: Prism[CopT[F], F[A8]] = new Prism[CopT[F], F[A8]] {
      def getOption(c: CopT[F]): Option[F[A8]] = c.run match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))) => Some(x)
        case _                                         => None
      }
      def reverseGet(x: F[A8]): CopT[F] = CopT(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))
    }

    implicit def inja7[F[_]]: Inj[CopT[F], F[A8]] = Inj.instance(prisma7.reverseGet(_))
    implicit def inja7Inverse[F[_]]: Inj[Option[F[A8]], CopT[F]] = Inj.instance(prisma7.getOption(_))

    implicit def prisma8[F[_]]: Prism[CopT[F], F[A9]] = new Prism[CopT[F], F[A9]] {
      def getOption(c: CopT[F]): Option[F[A9]] = c.run match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))) => Some(x)
        case _                                              => None
      }
      def reverseGet(x: F[A9]): CopT[F] = CopT(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))
    }

    implicit def inja8[F[_]]: Inj[CopT[F], F[A9]] = Inj.instance(prisma8.reverseGet(_))
    implicit def inja8Inverse[F[_]]: Inj[Option[F[A9]], CopT[F]] = Inj.instance(prisma8.getOption(_))

    implicit def prisma9[F[_]]: Prism[CopT[F], F[A10]] = new Prism[CopT[F], F[A10]] {
      def getOption(c: CopT[F]): Option[F[A10]] = c.run match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x))))))))) => Some(x)
        case _                                              => None
      }
      def reverseGet(x: F[A10]): CopT[F] = CopT(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x))))))))))
    }

    implicit def inja9[F[_]]: Inj[CopT[F], F[A10]] = Inj.instance(prisma9.reverseGet(_))
    implicit def inja9Inverse[F[_]]: Inj[Option[F[A10]], CopT[F]] = Inj.instance(prisma9.getOption(_))

  }

  type Cop[F[_]] = CopT[F]

  def combine[F[_], G[_]](implicit a0: G[F[A1]], a1: G[F[A2]], a2: G[F[A3]], a3: G[F[A4]], a4: G[F[A5]], a5: G[F[A6]], a6: G[F[A7]], a7: G[F[A8]], a8: G[F[A9]], a9: G[F[A10]])
    : ComposeAndXor[F, G, Cop, Prod] =
    new ComposeAndXor[F, G, Cop, Prod] {
      def mkChoose[B](f: B => Cop[F])(implicit d: Decidable[G]): G[B] =
        Combine.choose10(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9)(f(_).run)

      def mkAlt[B](f: Cop[F] => B)(implicit a: Alt[G]): G[B] =
        Combine.altly10(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9)(x => f(CopT(x)))

      def mkDivide[B](f: B => Prod[F])(implicit d: Divide[G]): G[B] =
        Combine.divide10(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9)(f(_).run)

      def mkApply[B](f: Prod[F] => B)(implicit a: Apply[G]): G[B] =
        Combine.apply10(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9) {
          case (i0, (i1, (i2, (i3, (i4, (i5, (i6, (i7, (i8, i9))))))))) =>
            f(ProdT((i0, (i1, (i2, (i3, (i4, (i5, (i6, (i7, (i8, i9)))))))))))
        }
    }

  def injEv[F[_]] = combine[F, Inj.Aux[Cop[F]]#Out].choose
  def liftEv[F[_]](implicit M: Monoid[Prod[F]]): Inj[Prod[F], Prod[F]] = combine[F, Inj.Aux[Prod[F]]#Out].divide

  def transformP[F[_], G[_]](nt: (F ~> G)): AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]#Prod[F] => AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]#Prod[G] =
    (p: AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]#Prod[F]) => {
      val pr = p.run
      ProdT[G]((nt(pr.t1), (nt(pr.t2), (nt(pr.t3), (nt(pr.t4), (nt(pr.t5), (nt(pr.t6), (nt(pr.t7), (nt(pr.t8), (nt(pr.t9), nt(pr.t10)))))))))))
    }

  def transformC[F[_], G[_]](nt: (F ~> G)): AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]#Cop[F] => AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]#Cop[G] =
    (p: AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]#Cop[F]) =>
      CopT[G](p.run.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), nt(_)))))))))))

  // format: off
  def sequenceP[F[_]](prod: Prod[F])(implicit A: Apply[F]): F[Prod[Id]] = {
    val p = prod.run
    A.map(
    A.ap(p.t10)(
    A.ap(p.t9)(
    A.ap(p.t8)(
    A.ap(p.t7)(
    A.ap(p.t6)(
    A.ap(p.t5)(
    A.ap(p.t4)(
    A.ap(p.t3)(
    A.ap(p.t2)(
    A.map(p.t1)((i0: A1) => (i1: A2) => (i2: A3) => (i3: A4) => (i4: A5) => (i5: A6) => (i6: A7) => (i7: A8) => (i8: A9) => (i9: A10) =>
      (i0, (i1, (i2, (i3, (i4, (i5, (i6, (i7, (i8, i9))))))))))))))))))))(ProdT[Id](_))
  }

  def sequenceC[F[_]](cop: Cop[F])(implicit FF: Functor[F]): F[Cop[Id]] =
    cop.run match {
      case -\/(x) => FF.map(FF.map(x)(y => -\/(y)))(CopT[Id](_))
      case \/-(-\/(x)) => FF.map(FF.map(x)(y => \/-(-\/(y))))(CopT[Id](_))
      case \/-(\/-(-\/(x))) => FF.map(FF.map(x)(y => \/-(\/-(-\/(y)))))(CopT[Id](_))
      case \/-(\/-(\/-(-\/(x)))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(-\/(y))))))(CopT[Id](_))
      case \/-(\/-(\/-(\/-(-\/(x))))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(\/-(-\/(y)))))))(CopT[Id](_))
      case \/-(\/-(\/-(\/-(\/-(-\/(x)))))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(\/-(\/-(-\/(y))))))))(CopT[Id](_))
      case \/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(\/-(\/-(\/-(-\/(y)))))))))(CopT[Id](_))
      case \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(y))))))))))(CopT[Id](_))
      case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(y)))))))))))(CopT[Id](_))
      case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x))))))))) => FF.map(FF.map(x)(y => \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(y)))))))))))(CopT[Id](_))
    }

  def extractC[F[_], B](c: Cop[F])(implicit inj: Inj[Option[B], Cop[F]]): Option[B] = inj(c)

  def extractP[F[_], B](p: Prod[F])(implicit inj: Inj[B, Prod[F]]): B = inj(p)

  def foldMap[F[_], C](p: Prod[F])(map: Cop[F] => C)(implicit M: Monoid[C]): C = {
    val pr = p.run
    M.append(map(CopT.inja0(pr.t1)), M.append(map(CopT.inja1(pr.t2)), M.append(map(CopT.inja2(pr.t3)), M.append(map(CopT.inja3(pr.t4)), M.append(map(CopT.inja4(pr.t5)), M.append(map(CopT.inja5(pr.t6)), M.append(map(CopT.inja6(pr.t7)), M.append(map(CopT.inja7(pr.t8)), M.append(map(CopT.inja8(pr.t9)), map(CopT.inja9(pr.t10)))))))))))
  }

  def foldMapId[F[_], C](p: Prod[F])(map: Cop[Id] => C)(
      implicit O: Ordering[Cop[Id]], M: Monoid[C], PE: PlusEmpty[F], U: Uncons[F]): C = {
    import scala.collection.mutable.{PriorityQueue => PQ}
    def uncons(p: Prod[F]): (List[Cop[Id]], Prod[F]) = {
      val pr = p.run
      val ht1 = U(pr.t1)
      val ht2 = U(pr.t2)
      val ht3 = U(pr.t3)
      val ht4 = U(pr.t4)
      val ht5 = U(pr.t5)
      val ht6 = U(pr.t6)
      val ht7 = U(pr.t7)
      val ht8 = U(pr.t8)
      val ht9 = U(pr.t9)
      val ht10 = U(pr.t10)
      (List(ht1._1.map(inj(_: Id[A1])), ht2._1.map(inj(_: Id[A2])), ht3._1.map(inj(_: Id[A3])), ht4._1.map(inj(_: Id[A4])), ht5._1.map(inj(_: Id[A5])), ht6._1.map(inj(_: Id[A6])), ht7._1.map(inj(_: Id[A7])), ht8._1.map(inj(_: Id[A8])), ht9._1.map(inj(_: Id[A9])), ht10._1.map(inj(_: Id[A10]))).flatten,
        ProdT[F]((ht1._2, (ht2._2, (ht3._2, (ht4._2, (ht5._2, (ht6._2, (ht7._2, (ht8._2, (ht9._2, ht10._2)))))))))))
    }
    @scala.annotation.tailrec
    def go(prod: Prod[F], q: PQ[Cop[Id]], out: C): C =
      (prod.run.==((PE.empty[A1], (PE.empty[A2], (PE.empty[A3], (PE.empty[A4], (PE.empty[A5], (PE.empty[A6], (PE.empty[A7], (PE.empty[A8], (PE.empty[A9], PE.empty[A10]))))))))))) match {
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
              go(ProdT[F]((t, (pr.t2, (pr.t3, (pr.t4, (pr.t5, (pr.t6, (pr.t7, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A1])), M.append(out, map(inj(x))))
          }
          case \/-(-\/(x)) => {
              val pr = prod.run
              val (h, t) = U(pr.t2)
              go(ProdT[F]((pr.t1, (t, (pr.t3, (pr.t4, (pr.t5, (pr.t6, (pr.t7, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A2])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(-\/(x))) => {
              val pr = prod.run
              val (h, t) = U(pr.t3)
              go(ProdT[F]((pr.t1, (pr.t2, (t, (pr.t4, (pr.t5, (pr.t6, (pr.t7, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A3])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(-\/(x)))) => {
              val pr = prod.run
              val (h, t) = U(pr.t4)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (t, (pr.t5, (pr.t6, (pr.t7, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A4])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(\/-(-\/(x))))) => {
              val pr = prod.run
              val (h, t) = U(pr.t5)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (pr.t4, (t, (pr.t6, (pr.t7, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A5])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(\/-(\/-(-\/(x)))))) => {
              val pr = prod.run
              val (h, t) = U(pr.t6)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (pr.t4, (pr.t5, (t, (pr.t7, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A6])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))) => {
              val pr = prod.run
              val (h, t) = U(pr.t7)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (pr.t4, (pr.t5, (pr.t6, (t, (pr.t8, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A7])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))) => {
              val pr = prod.run
              val (h, t) = U(pr.t8)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (pr.t4, (pr.t5, (pr.t6, (pr.t7, (t, (pr.t9, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A8])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))) => {
              val pr = prod.run
              val (h, t) = U(pr.t9)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (pr.t4, (pr.t5, (pr.t6, (pr.t7, (pr.t8, (t, pr.t10)))))))))),
                q ++= h.map(inj(_: Id[A9])), M.append(out, map(inj(x))))
          }
          case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x))))))))) => {
              val pr = prod.run
              val (h, t) = U(pr.t10)
              go(ProdT[F]((pr.t1, (pr.t2, (pr.t3, (pr.t4, (pr.t5, (pr.t6, (pr.t7, (pr.t8, (pr.t9, t)))))))))),
                q ++= h.map(inj(_: Id[A10])), M.append(out, map(inj(x))))
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

object AndXor10 {
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]: AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] =
    new AndXor10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] {}
}
