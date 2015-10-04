import scala.collection._

object learningconcurrency {
  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")
}

object SynchronizedBadPool extends App {
  import learningconcurrency._

  type Task = () => Unit

  private val tasks = mutable.Queue[Task]()

  val worker = new Thread {

    def poll(): Option[Task] = tasks.synchronized {
      if(tasks.nonEmpty) Some(tasks.dequeue()) else None
    }

    override def run() = while(true) poll() match {
      case Some(task) => task()
      case None       =>
    }

  }

  worker.setName("worker")
  worker.setDaemon(true)
  worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
  }

  asynchronous { log("Hello")  }
  asynchronous { log(" world") }

  Thread.sleep(5000)
}