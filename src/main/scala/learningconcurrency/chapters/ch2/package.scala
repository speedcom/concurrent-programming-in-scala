package learningconcurrency.chapters


package object ch2 {

  def thread[T](block: => T): Thread = {
    val t = new Thread {
      override def run(): Unit = block
    }
    t.start()
    t
  }

  def log(msg: String): Unit = println(s"${Thread.currentThread().getName}: $msg")

}