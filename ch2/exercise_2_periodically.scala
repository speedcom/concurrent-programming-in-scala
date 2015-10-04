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

// TODO: not finished
def periodically(duration: Long)(b: =>Unit): Unit = {

  val thread = thread {

    while(true) {

    }

  }



}
