package andxor
import scala.language.higherKinds
import scalaz.{Apply, PlusEmpty, Monoid, \/, -\/, \/-, ~>}
import scalaz.Id.Id

trait AndXorK21[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] extends AndXor {
  type Prod = (F[A1], F[A2], F[A3], F[A4], F[A5], F[A6], F[A7], F[A8], F[A9], F[A10], F[A11], F[A12], F[A13], F[A14], F[A15], F[A16], F[A17], F[A18], F[A19], F[A20], F[A21])
  type Cop = (F[A1] \/ (F[A2] \/ (F[A3] \/ (F[A4] \/ (F[A5] \/ (F[A6] \/ (F[A7] \/ (F[A8] \/ (F[A9] \/ (F[A10] \/ (F[A11] \/ (F[A12] \/ (F[A13] \/ (F[A14] \/ (F[A15] \/ (F[A16] \/ (F[A17] \/ (F[A18] \/ (F[
    A19
  ] \/ (F[A20] \/ F[A21]))))))))))))))))))))
  val AndXorF = AndXorF21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
  type AndXor[G[_]] = AndXorF21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Repr[G]
  def combine[G[_]](
      implicit a0: G[F[A1]],
      a1: G[F[A2]],
      a2: G[F[A3]],
      a3: G[F[A4]],
      a4: G[F[A5]],
      a5: G[F[A6]],
      a6: G[F[A7]],
      a7: G[F[A8]],
      a8: G[F[A9]],
      a9: G[F[A10]],
      a10: G[F[A11]],
      a11: G[F[A12]],
      a12: G[F[A13]],
      a13: G[F[A14]],
      a14: G[F[A15]],
      a15: G[F[A16]],
      a16: G[F[A17]],
      a17: G[F[A18]],
      a18: G[F[A19]],
      a19: G[F[A20]],
      a20: G[F[A21]]
  ): ComposeAndXor[G, Cop, Prod] =
    new ComposeAndXor[G, Cop, Prod] {
      def mkChoose[B](f: B => Cop)(implicit d: Decidable[G]): G[B] =
        Combine.choose21(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)(f)
      def mkAlt[B](f: Cop => B)(implicit a: Alt[G]): G[B] =
        Combine.altly21(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)(f)
      def mkDivide[B](f: B => Prod)(implicit d: Divide[G]): G[B] =
        Combine.divide21(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)(f)
      def mkApply[B](f: Prod => B)(implicit a: Apply[G]): G[B] =
        Combine.apply21(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)(
          (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20) =>
            f((i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20))
        )
    }

  object instances {

    implicit val prisma0: Prism[Cop, F[A1]] = new Prism[Cop, F[A1]] {
      def getOption(c: Cop): Option[F[A1]] = c match {
        case -\/(x) => Some(x)
        case _      => None
      }
      def reverseGet(x: F[A1]): Cop = -\/(x)
    }

    implicit val inja0: Inj[Cop, F[A1]] = Inj.instance(prisma0.reverseGet(_))
    implicit val inja0Inverse: Inj[Option[F[A1]], Cop] = Inj.instance(prisma0.getOption(_))

    implicit val prisma1: Prism[Cop, F[A2]] = new Prism[Cop, F[A2]] {
      def getOption(c: Cop): Option[F[A2]] = c match {
        case \/-(-\/(x)) => Some(x)
        case _           => None
      }
      def reverseGet(x: F[A2]): Cop = \/-(-\/(x))
    }

    implicit val inja1: Inj[Cop, F[A2]] = Inj.instance(prisma1.reverseGet(_))
    implicit val inja1Inverse: Inj[Option[F[A2]], Cop] = Inj.instance(prisma1.getOption(_))

    implicit val prisma2: Prism[Cop, F[A3]] = new Prism[Cop, F[A3]] {
      def getOption(c: Cop): Option[F[A3]] = c match {
        case \/-(\/-(-\/(x))) => Some(x)
        case _                => None
      }
      def reverseGet(x: F[A3]): Cop = \/-(\/-(-\/(x)))
    }

    implicit val inja2: Inj[Cop, F[A3]] = Inj.instance(prisma2.reverseGet(_))
    implicit val inja2Inverse: Inj[Option[F[A3]], Cop] = Inj.instance(prisma2.getOption(_))

    implicit val prisma3: Prism[Cop, F[A4]] = new Prism[Cop, F[A4]] {
      def getOption(c: Cop): Option[F[A4]] = c match {
        case \/-(\/-(\/-(-\/(x)))) => Some(x)
        case _                     => None
      }
      def reverseGet(x: F[A4]): Cop = \/-(\/-(\/-(-\/(x))))
    }

    implicit val inja3: Inj[Cop, F[A4]] = Inj.instance(prisma3.reverseGet(_))
    implicit val inja3Inverse: Inj[Option[F[A4]], Cop] = Inj.instance(prisma3.getOption(_))

    implicit val prisma4: Prism[Cop, F[A5]] = new Prism[Cop, F[A5]] {
      def getOption(c: Cop): Option[F[A5]] = c match {
        case \/-(\/-(\/-(\/-(-\/(x))))) => Some(x)
        case _                          => None
      }
      def reverseGet(x: F[A5]): Cop = \/-(\/-(\/-(\/-(-\/(x)))))
    }

    implicit val inja4: Inj[Cop, F[A5]] = Inj.instance(prisma4.reverseGet(_))
    implicit val inja4Inverse: Inj[Option[F[A5]], Cop] = Inj.instance(prisma4.getOption(_))

    implicit val prisma5: Prism[Cop, F[A6]] = new Prism[Cop, F[A6]] {
      def getOption(c: Cop): Option[F[A6]] = c match {
        case \/-(\/-(\/-(\/-(\/-(-\/(x)))))) => Some(x)
        case _                               => None
      }
      def reverseGet(x: F[A6]): Cop = \/-(\/-(\/-(\/-(\/-(-\/(x))))))
    }

    implicit val inja5: Inj[Cop, F[A6]] = Inj.instance(prisma5.reverseGet(_))
    implicit val inja5Inverse: Inj[Option[F[A6]], Cop] = Inj.instance(prisma5.getOption(_))

    implicit val prisma6: Prism[Cop, F[A7]] = new Prism[Cop, F[A7]] {
      def getOption(c: Cop): Option[F[A7]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))) => Some(x)
        case _                                    => None
      }
      def reverseGet(x: F[A7]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))
    }

    implicit val inja6: Inj[Cop, F[A7]] = Inj.instance(prisma6.reverseGet(_))
    implicit val inja6Inverse: Inj[Option[F[A7]], Cop] = Inj.instance(prisma6.getOption(_))

    implicit val prisma7: Prism[Cop, F[A8]] = new Prism[Cop, F[A8]] {
      def getOption(c: Cop): Option[F[A8]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))) => Some(x)
        case _                                         => None
      }
      def reverseGet(x: F[A8]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))
    }

    implicit val inja7: Inj[Cop, F[A8]] = Inj.instance(prisma7.reverseGet(_))
    implicit val inja7Inverse: Inj[Option[F[A8]], Cop] = Inj.instance(prisma7.getOption(_))

    implicit val prisma8: Prism[Cop, F[A9]] = new Prism[Cop, F[A9]] {
      def getOption(c: Cop): Option[F[A9]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))) => Some(x)
        case _                                              => None
      }
      def reverseGet(x: F[A9]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))
    }

    implicit val inja8: Inj[Cop, F[A9]] = Inj.instance(prisma8.reverseGet(_))
    implicit val inja8Inverse: Inj[Option[F[A9]], Cop] = Inj.instance(prisma8.getOption(_))

    implicit val prisma9: Prism[Cop, F[A10]] = new Prism[Cop, F[A10]] {
      def getOption(c: Cop): Option[F[A10]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))) => Some(x)
        case _                                                   => None
      }
      def reverseGet(x: F[A10]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))
    }

    implicit val inja9: Inj[Cop, F[A10]] = Inj.instance(prisma9.reverseGet(_))
    implicit val inja9Inverse: Inj[Option[F[A10]], Cop] = Inj.instance(prisma9.getOption(_))

    implicit val prisma10: Prism[Cop, F[A11]] = new Prism[Cop, F[A11]] {
      def getOption(c: Cop): Option[F[A11]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))) => Some(x)
        case _                                                        => None
      }
      def reverseGet(x: F[A11]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))
    }

    implicit val inja10: Inj[Cop, F[A11]] = Inj.instance(prisma10.reverseGet(_))
    implicit val inja10Inverse: Inj[Option[F[A11]], Cop] = Inj.instance(prisma10.getOption(_))

    implicit val prisma11: Prism[Cop, F[A12]] = new Prism[Cop, F[A12]] {
      def getOption(c: Cop): Option[F[A12]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))) => Some(x)
        case _                                                             => None
      }
      def reverseGet(x: F[A12]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))
    }

    implicit val inja11: Inj[Cop, F[A12]] = Inj.instance(prisma11.reverseGet(_))
    implicit val inja11Inverse: Inj[Option[F[A12]], Cop] = Inj.instance(prisma11.getOption(_))

    implicit val prisma12: Prism[Cop, F[A13]] = new Prism[Cop, F[A13]] {
      def getOption(c: Cop): Option[F[A13]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))) => Some(x)
        case _                                                                  => None
      }
      def reverseGet(x: F[A13]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))
    }

    implicit val inja12: Inj[Cop, F[A13]] = Inj.instance(prisma12.reverseGet(_))
    implicit val inja12Inverse: Inj[Option[F[A13]], Cop] = Inj.instance(prisma12.getOption(_))

    implicit val prisma13: Prism[Cop, F[A14]] = new Prism[Cop, F[A14]] {
      def getOption(c: Cop): Option[F[A14]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))) => Some(x)
        case _                                                                       => None
      }
      def reverseGet(x: F[A14]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))
    }

    implicit val inja13: Inj[Cop, F[A14]] = Inj.instance(prisma13.reverseGet(_))
    implicit val inja13Inverse: Inj[Option[F[A14]], Cop] = Inj.instance(prisma13.getOption(_))

    implicit val prisma14: Prism[Cop, F[A15]] = new Prism[Cop, F[A15]] {
      def getOption(c: Cop): Option[F[A15]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))) => Some(x)
        case _                                                                            => None
      }
      def reverseGet(x: F[A15]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))
    }

    implicit val inja14: Inj[Cop, F[A15]] = Inj.instance(prisma14.reverseGet(_))
    implicit val inja14Inverse: Inj[Option[F[A15]], Cop] = Inj.instance(prisma14.getOption(_))

    implicit val prisma15: Prism[Cop, F[A16]] = new Prism[Cop, F[A16]] {
      def getOption(c: Cop): Option[F[A16]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))) => Some(x)
        case _                                                                                 => None
      }
      def reverseGet(x: F[A16]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))
    }

    implicit val inja15: Inj[Cop, F[A16]] = Inj.instance(prisma15.reverseGet(_))
    implicit val inja15Inverse: Inj[Option[F[A16]], Cop] = Inj.instance(prisma15.getOption(_))

    implicit val prisma16: Prism[Cop, F[A17]] = new Prism[Cop, F[A17]] {
      def getOption(c: Cop): Option[F[A17]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))) => Some(x)
        case _                                                                                      => None
      }
      def reverseGet(x: F[A17]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))))
    }

    implicit val inja16: Inj[Cop, F[A17]] = Inj.instance(prisma16.reverseGet(_))
    implicit val inja16Inverse: Inj[Option[F[A17]], Cop] = Inj.instance(prisma16.getOption(_))

    implicit val prisma17: Prism[Cop, F[A18]] = new Prism[Cop, F[A18]] {
      def getOption(c: Cop): Option[F[A18]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))))) => Some(x)
        case _                                                                                           => None
      }
      def reverseGet(x: F[A18]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))))
    }

    implicit val inja17: Inj[Cop, F[A18]] = Inj.instance(prisma17.reverseGet(_))
    implicit val inja17Inverse: Inj[Option[F[A18]], Cop] = Inj.instance(prisma17.getOption(_))

    implicit val prisma18: Prism[Cop, F[A19]] = new Prism[Cop, F[A19]] {
      def getOption(c: Cop): Option[F[A19]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))))) => Some(x)
        case _                                                                                                => None
      }
      def reverseGet(x: F[A19]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))))))
    }

    implicit val inja18: Inj[Cop, F[A19]] = Inj.instance(prisma18.reverseGet(_))
    implicit val inja18Inverse: Inj[Option[F[A19]], Cop] = Inj.instance(prisma18.getOption(_))

    implicit val prisma19: Prism[Cop, F[A20]] = new Prism[Cop, F[A20]] {
      def getOption(c: Cop): Option[F[A20]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))))))) => Some(x)
        case _                                                                                                     => None
      }
      def reverseGet(x: F[A20]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))))))
    }

    implicit val inja19: Inj[Cop, F[A20]] = Inj.instance(prisma19.reverseGet(_))
    implicit val inja19Inverse: Inj[Option[F[A20]], Cop] = Inj.instance(prisma19.getOption(_))

    implicit val prisma20: Prism[Cop, F[A21]] = new Prism[Cop, F[A21]] {
      def getOption(c: Cop): Option[F[A21]] = c match {
        case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x)))))))))))))))))))) => Some(x)
        case _                                                                                                     => None
      }
      def reverseGet(x: F[A21]): Cop = \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x))))))))))))))))))))
    }

    implicit val inja20: Inj[Cop, F[A21]] = Inj.instance(prisma20.reverseGet(_))
    implicit val inja20Inverse: Inj[Option[F[A21]], Cop] = Inj.instance(prisma20.getOption(_))

    implicit def lifta0(implicit M: Monoid[Prod]): Inj[Prod, F[A1]] = {
      val (_, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((_, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta0Inverse: Inj[F[A1], Prod] = Inj.instance(_._1)

    implicit def lifta1(implicit M: Monoid[Prod]): Inj[Prod, F[A2]] = {
      val (a0, _, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, _, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta1Inverse: Inj[F[A2], Prod] = Inj.instance(_._2)

    implicit def lifta2(implicit M: Monoid[Prod]): Inj[Prod, F[A3]] = {
      val (a0, a1, _, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, _, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta2Inverse: Inj[F[A3], Prod] = Inj.instance(_._3)

    implicit def lifta3(implicit M: Monoid[Prod]): Inj[Prod, F[A4]] = {
      val (a0, a1, a2, _, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, _, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta3Inverse: Inj[F[A4], Prod] = Inj.instance(_._4)

    implicit def lifta4(implicit M: Monoid[Prod]): Inj[Prod, F[A5]] = {
      val (a0, a1, a2, a3, _, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, _, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta4Inverse: Inj[F[A5], Prod] = Inj.instance(_._5)

    implicit def lifta5(implicit M: Monoid[Prod]): Inj[Prod, F[A6]] = {
      val (a0, a1, a2, a3, a4, _, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, _, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta5Inverse: Inj[F[A6], Prod] = Inj.instance(_._6)

    implicit def lifta6(implicit M: Monoid[Prod]): Inj[Prod, F[A7]] = {
      val (a0, a1, a2, a3, a4, a5, _, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, _, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta6Inverse: Inj[F[A7], Prod] = Inj.instance(_._7)

    implicit def lifta7(implicit M: Monoid[Prod]): Inj[Prod, F[A8]] = {
      val (a0, a1, a2, a3, a4, a5, a6, _, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, _, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta7Inverse: Inj[F[A8], Prod] = Inj.instance(_._8)

    implicit def lifta8(implicit M: Monoid[Prod]): Inj[Prod, F[A9]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, _, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, _, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta8Inverse: Inj[F[A9], Prod] = Inj.instance(_._9)

    implicit def lifta9(implicit M: Monoid[Prod]): Inj[Prod, F[A10]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, _, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, _, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta9Inverse: Inj[F[A10], Prod] = Inj.instance(_._10)

    implicit def lifta10(implicit M: Monoid[Prod]): Inj[Prod, F[A11]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, _, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, _, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta10Inverse: Inj[F[A11], Prod] = Inj.instance(_._11)

    implicit def lifta11(implicit M: Monoid[Prod]): Inj[Prod, F[A12]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, _, a11, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, _, a11, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta11Inverse: Inj[F[A12], Prod] = Inj.instance(_._12)

    implicit def lifta12(implicit M: Monoid[Prod]): Inj[Prod, F[A13]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, _, a12, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, _, a12, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta12Inverse: Inj[F[A13], Prod] = Inj.instance(_._13)

    implicit def lifta13(implicit M: Monoid[Prod]): Inj[Prod, F[A14]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, _, a13, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, _, a13, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta13Inverse: Inj[F[A14], Prod] = Inj.instance(_._14)

    implicit def lifta14(implicit M: Monoid[Prod]): Inj[Prod, F[A15]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, _, a14, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, _, a14, a15, a16, a17, a18, a19))
    }

    implicit val lifta14Inverse: Inj[F[A15], Prod] = Inj.instance(_._15)

    implicit def lifta15(implicit M: Monoid[Prod]): Inj[Prod, F[A16]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, _, a15, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, _, a15, a16, a17, a18, a19))
    }

    implicit val lifta15Inverse: Inj[F[A16], Prod] = Inj.instance(_._16)

    implicit def lifta16(implicit M: Monoid[Prod]): Inj[Prod, F[A17]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, _, a16, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, _, a16, a17, a18, a19))
    }

    implicit val lifta16Inverse: Inj[F[A17], Prod] = Inj.instance(_._17)

    implicit def lifta17(implicit M: Monoid[Prod]): Inj[Prod, F[A18]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, _, a17, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, _, a17, a18, a19))
    }

    implicit val lifta17Inverse: Inj[F[A18], Prod] = Inj.instance(_._18)

    implicit def lifta18(implicit M: Monoid[Prod]): Inj[Prod, F[A19]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, _, a18, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, _, a18, a19))
    }

    implicit val lifta18Inverse: Inj[F[A19], Prod] = Inj.instance(_._19)

    implicit def lifta19(implicit M: Monoid[Prod]): Inj[Prod, F[A20]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, _, a19) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, _, a19))
    }

    implicit val lifta19Inverse: Inj[F[A20], Prod] = Inj.instance(_._20)

    implicit def lifta20(implicit M: Monoid[Prod]): Inj[Prod, F[A21]] = {
      val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, _) =
        M.zero
      Inj.instance((a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, _))
    }

    implicit val lifta20Inverse: Inj[F[A21], Prod] = Inj.instance(_._21)

  }

  import instances._

  val injEv = combine[Inj.Aux[Cop]#Out].choose
  def liftEv(implicit M: Monoid[Prod]): Inj[Prod, Prod] = combine[Inj.Aux[Prod]#Out].divide

  def transformP[G[_]](nt: (F ~> G)): AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Prod => AndXorK21[
    G,
    A1,
    A2,
    A3,
    A4,
    A5,
    A6,
    A7,
    A8,
    A9,
    A10,
    A11,
    A12,
    A13,
    A14,
    A15,
    A16,
    A17,
    A18,
    A19,
    A20,
    A21
  ]#Prod =
    (p: AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Prod) =>
      (
        nt(p._1),
        nt(p._2),
        nt(p._3),
        nt(p._4),
        nt(p._5),
        nt(p._6),
        nt(p._7),
        nt(p._8),
        nt(p._9),
        nt(p._10),
        nt(p._11),
        nt(p._12),
        nt(p._13),
        nt(p._14),
        nt(p._15),
        nt(p._16),
        nt(p._17),
        nt(p._18),
        nt(p._19),
        nt(p._20),
        nt(p._21)
      )

  def transformC[G[_]](nt: (F ~> G)): AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Cop => AndXorK21[
    G,
    A1,
    A2,
    A3,
    A4,
    A5,
    A6,
    A7,
    A8,
    A9,
    A10,
    A11,
    A12,
    A13,
    A14,
    A15,
    A16,
    A17,
    A18,
    A19,
    A20,
    A21
  ]#Cop =
    (p: AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Cop) =>
      p.bimap(
        nt(_),
        _.bimap(
          nt(_),
          _.bimap(
            nt(_),
            _.bimap(
              nt(_),
              _.bimap(
                nt(_),
                _.bimap(
                  nt(_),
                  _.bimap(
                    nt(_),
                    _.bimap(
                      nt(_),
                      _.bimap(
                        nt(_),
                        _.bimap(
                          nt(_),
                          _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), _.bimap(nt(_), nt(_)))))))))))
                        )
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )

  // format: off
  def sequenceP(prod: Prod)(implicit A: Apply[F]): F[AndXorK21[Id, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Prod] = {
    val (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) = prod
    A.ap(a20)(
    A.ap(a19)(
    A.ap(a18)(
    A.ap(a17)(
    A.ap(a16)(
    A.ap(a15)(
    A.ap(a14)(
    A.ap(a13)(
    A.ap(a12)(
    A.ap(a11)(
    A.ap(a10)(
    A.ap(a9)(
    A.ap(a8)(
    A.ap(a7)(
    A.ap(a6)(
    A.ap(a5)(
    A.ap(a4)(
    A.ap(a3)(
    A.ap(a2)(
    A.ap(a1)(
     A.map(a0)(((i0: A1, i1: A2, i2: A3, i3: A4, i4: A5, i5: A6, i6: A7, i7: A8, i8: A9, i9: A10, i10: A11, i11: A12, i12: A13, i13: A14, i14: A15, i15: A16, i16: A17, i17: A18, i18: A19, i19: A20, i20: A21) =>
    (i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20)).curried)))))))))))))))))))))
  }
  

  def extractC[B](c: Cop)(implicit inj: Inj[Option[B], Cop]): Option[B] = inj(c)

  def extractP[B](p: Prod)(implicit inj: Inj[B, Prod]): B = inj(p)

  def foldMap[G[_], C](p: AndXor[G]#Prod)(
    map: AndXor[Id]#Cop => C)(
    implicit O: Ordering[AndXorK21[Id, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]#Cop], M: Monoid[C],
    PE: PlusEmpty[G], U: Uncons[G]): C = {
    val TG = AndXorF[G]
    val TI = AndXorF[Id]
    import scala.collection.mutable.{PriorityQueue => PQ}
    import TI.instances._
    def uncons(p: TG.Prod): (List[TI.Cop], TG.Prod) = {
     val hts = (U(p._1), U(p._2), U(p._3), U(p._4), U(p._5), U(p._6), U(p._7), U(p._8), U(p._9), U(p._10), U(p._11), U(p._12), U(p._13), U(p._14), U(p._15), U(p._16), U(p._17), U(p._18), U(p._19), U(p._20), U(p._21))
     (List(hts._1._1.map(TI.inj(_: A1)), hts._2._1.map(TI.inj(_: A2)), hts._3._1.map(TI.inj(_: A3)), hts._4._1.map(TI.inj(_: A4)), hts._5._1.map(TI.inj(_: A5)), hts._6._1.map(TI.inj(_: A6)), hts._7._1.map(TI.inj(_: A7)), hts._8._1.map(TI.inj(_: A8)), hts._9._1.map(TI.inj(_: A9)), hts._10._1.map(TI.inj(_: A10)), hts._11._1.map(TI.inj(_: A11)), hts._12._1.map(TI.inj(_: A12)), hts._13._1.map(TI.inj(_: A13)), hts._14._1.map(TI.inj(_: A14)), hts._15._1.map(TI.inj(_: A15)), hts._16._1.map(TI.inj(_: A16)), hts._17._1.map(TI.inj(_: A17)), hts._18._1.map(TI.inj(_: A18)), hts._19._1.map(TI.inj(_: A19)), hts._20._1.map(TI.inj(_: A20)), hts._21._1.map(TI.inj(_: A21))).flatten,
      (hts._1._2, hts._2._2, hts._3._2, hts._4._2, hts._5._2, hts._6._2, hts._7._2, hts._8._2, hts._9._2, hts._10._2, hts._11._2, hts._12._2, hts._13._2, hts._14._2, hts._15._2, hts._16._2, hts._17._2, hts._18._2, hts._19._2, hts._20._2, hts._21._2))
    }
    @scala.annotation.tailrec
    def go(prod: TG.Prod, q: PQ[TI.Cop], out: C): C =
     (prod.==((PE.empty[A1], PE.empty[A2], PE.empty[A3], PE.empty[A4], PE.empty[A5], PE.empty[A6], PE.empty[A7], PE.empty[A8], PE.empty[A9], PE.empty[A10], PE.empty[A11], PE.empty[A12], PE.empty[A13], PE.empty[A14], PE.empty[A15], PE.empty[A16], PE.empty[A17], PE.empty[A18], PE.empty[A19], PE.empty[A20], PE.empty[A21]))) match {
       case true =>
         q.foldLeft(out)((acc, el) => M.append(acc, map(el)))
       case false => q.isEmpty match {
         case true => {
           val (hs, ts) = uncons(prod)
           q ++= hs
           go(ts, q, out)
         }
         case false => q.dequeue match {
                        case -\/(x) => {
               val (h, t) = U(prod._1)
               go((t, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(-\/(x)) => {
               val (h, t) = U(prod._2)
               go((prod._1, t, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(-\/(x))) => {
               val (h, t) = U(prod._3)
               go((prod._1, prod._2, t, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(-\/(x)))) => {
               val (h, t) = U(prod._4)
               go((prod._1, prod._2, prod._3, t, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(-\/(x))))) => {
               val (h, t) = U(prod._5)
               go((prod._1, prod._2, prod._3, prod._4, t, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(-\/(x)))))) => {
               val (h, t) = U(prod._6)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, t, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))) => {
               val (h, t) = U(prod._7)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, t, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))) => {
               val (h, t) = U(prod._8)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, t, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))) => {
               val (h, t) = U(prod._9)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, t, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))) => {
               val (h, t) = U(prod._10)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, t, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))) => {
               val (h, t) = U(prod._11)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, t, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))) => {
               val (h, t) = U(prod._12)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, t, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))) => {
               val (h, t) = U(prod._13)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, t, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))) => {
               val (h, t) = U(prod._14)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, t, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))) => {
               val (h, t) = U(prod._15)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, t, prod._16, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))) => {
               val (h, t) = U(prod._16)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, t, prod._17, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))) => {
               val (h, t) = U(prod._17)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, t, prod._18, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))))) => {
               val (h, t) = U(prod._18)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, t, prod._19, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x))))))))))))))))))) => {
               val (h, t) = U(prod._19)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, t, prod._20, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(-\/(x)))))))))))))))))))) => {
               val (h, t) = U(prod._20)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, t, prod._21),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }
             case \/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(\/-(x)))))))))))))))))))) => {
               val (h, t) = U(prod._21)
               go((prod._1, prod._2, prod._3, prod._4, prod._5, prod._6, prod._7, prod._8, prod._9, prod._10, prod._11, prod._12, prod._13, prod._14, prod._15, prod._16, prod._17, prod._18, prod._19, prod._20, t),
                 q ++= h.map(TI.inj(_)), M.append(out, map(TI.inj(x))))
             }

         }
       }
     }
    val Q = new scala.collection.mutable.PriorityQueue[TI.Cop]()
    val (hs, ts) = uncons(p)
    Q ++= hs
    go(ts, Q, M.zero)
  }
  // format: on
}

object AndXorK21 {

  def apply[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
    : AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] =
    new AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] {}
}

trait AndXorF21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] {
  type Repr[F[_]] = AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
  def apply[F[_]]: Repr[F] =
    new AndXorK21[F, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] {}
}

object AndXorF21 {
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
    : AndXorF21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] =
    new AndXorF21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] {}
}

trait AndXor21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
    extends AndXorK21[Id, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]

object AndXor21 {
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
    : AndXor21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] =
    new AndXor21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] {}

}
