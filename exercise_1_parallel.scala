object learningconcurrency {
  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")
}


def thread[A](a: => A): Thread = {

  val t = new Thread {
    override def run(): Unit = a
  }

  t.start()
  t
}


def parallel[A, B](a: => A, b: => B): (A, B) = {

  var aResult: Option[A] = None
  var bResult: Option[B] = None

  val t1 = thread { aResult = Some(a) }
  val t2 = thread { bResult = Some(b) }

  t1.join(); t2.join();

  (aResult.get, bResult.get)
}