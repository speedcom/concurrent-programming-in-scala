package learningconcurrency.chapters.ch2

import scala.collection.mutable

object SynchronizedBadPool extends App {
  type Task = () => Unit

  val tasks = mutable.Queue[Task]()

  val worker = new Thread {

    def poll(): Option[Task] = tasks.synchronized {
      if(tasks.nonEmpty)
        Some(tasks.dequeue())
      else
        None
    }

    // BUSY WAITING
    override def run(): Unit = while(true) poll() match {
      case Some(task) => task()
      case None =>
    }
  }

  worker.setName("Worker")
  worker.setDaemon(true)
  worker.start()

  def asynchronous(t: Task): Unit = tasks.synchronized {
    tasks.enqueue(t)
  }

  asynchronous(() => log("first test"))
  asynchronous(() => log("second test"))

  Thread.sleep(5000)
}
