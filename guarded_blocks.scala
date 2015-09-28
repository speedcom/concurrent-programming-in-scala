import scala.collection._

object SynchronizedBadPool extends App {
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


}