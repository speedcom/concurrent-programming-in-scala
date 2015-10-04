object SynchronizedPool extends App {
  import scala.collection._

  type Task = () => Unit

  private val tasks = mutable.Queue[Task]()

  object Worker extends Thread {

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

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
    tasks.notify()
  }

  asynchronous { println("Hello")   }
  asynchronous { println(" world!") }

  Thread.sleep(500)

}