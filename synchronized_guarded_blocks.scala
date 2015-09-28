object learningconcurrency {
  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")
}

object SynchronizedGuardedBlocks extends App {
  import learningconcurrency._

  def thread(block: => Unit) = {
    val t = new Thread {
      override def run() = block
    }
    t.start()
    t
  }

  var message: Option[String] = None

  val lock = new AnyRef

  val greeter = thread {
    lock.synchronized {
      lock.wait()
      log(message.get)
    }
  }

  thread {
    lock.synchronized {
      message = Some("some message")
      lock.notify()
    }
  }

  greeter.join()


}