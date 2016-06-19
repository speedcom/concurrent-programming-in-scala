package learningconcurrency.chapters.ch2

import scala.collection.mutable

object SynchronizedPool extends App {
  type Task = () => Unit
  private val tasks = mutable.Queue[Task]()

  object Worker extends Thread {

    setName("Worker")
    setDaemon(true)

    def poll() = tasks.synchronized {
      while(tasks.isEmpty)
        tasks.wait()
      tasks.dequeue()
    }

    override def run(): Unit = while(true) {
      val task = poll()
      task()
    }
  }

  Worker.start()

  def asynchronous(t:Task): Unit = tasks.synchronized {
    tasks.enqueue(t)
    tasks.notify()
  }

  asynchronous(() => log("Hello"))
  asynchronous(() => log("World"))

  Thread.sleep(5000)

}
