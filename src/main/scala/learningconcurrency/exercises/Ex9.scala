package learningconcurrency.exercises

import learningconcurrency.chapters.ch2._

/**
  * 9. Extend the PriorityTaskPool class from the previous exercise so that it
  * supports any number of worker threads p. The parameter p is specified in
  * the constructor of the PriorityTaskPool class.
  */
object Ex9 extends App {

  case class Task(priority: Int, task: () => Unit) extends Ordered[Task] {
    override def compare(that: Task): Int = {
      if (this.priority == that.priority)
        0
      else if (this.priority > that.priority)
        1
      else
        -1
    }
  }

  class PriorityTaskPool(p: Int) {

    import scala.collection.mutable.PriorityQueue

    private val tasks = new PriorityQueue[Task]()

    class Worker(idx: Int) extends Thread {

      setName(s"Worker $idx")
      setDaemon(true)

      def poll(): Task = tasks.synchronized {
        while (tasks.isEmpty)
          tasks.wait()
        tasks.dequeue()
      }

      override def run(): Unit = while (true) {
        val t = poll()
        t.task()
      }
    }

    def asynchronous(priority: Int)(task: => Unit): Unit = tasks.synchronized {
      tasks.enqueue(Task(priority, () => task))
      tasks.notify()
    }

    (1 to p) foreach { i => new Worker(i).start() }

  }

  val taskPool = new PriorityTaskPool(10)

  (1 to 100).foreach((i) => {
    val a = (Math.random*1000).toInt
    taskPool.asynchronous(a)({log(s"<- $a")})
  })

  Thread.sleep(1000)
}
