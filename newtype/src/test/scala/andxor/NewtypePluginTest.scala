package andxor.test

trait Show[A] { def apply(a: A): String }
object Show {
  def apply[A](a: A)(implicit s: Show[A]): String = s(a)
}

object types {
  @newtype type Test1 = String

  @newtype case class Test2(run: Int) {
    def extension: String = ""
  }

  class Test3() {
    @newtype case class Test4(run: Boolean) {
      def extension: String = ""
    }
  }

  @newtype case class Test4(run: String)
  object Test4 {
    implicit val show: Show[Test4] = new Show[Test4] { def apply(t: Test4): String = s"newtype Test4(${t.run})" }

    def printTest4(t: Test4): Unit = println(Show(t))
  }
}
