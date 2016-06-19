package learningconcurrency.exercises

import learningconcurrency.chapters.ch2.thread

/**
  * 8. Recall the asynchronous method from the Guarded blocks section. This
  * method stores the tasks in a First In First Out (FIFO) queue; before a
  * submitted task is executed, all the previously submitted tasks need to be
  * executed. In some cases, we want to assign priorities to tasks so that a
  * high-priority task can execute as soon as it is submitted to the task pool.
  * Implement a PriorityTaskPool class that has the asynchronous method
  * with the following signature:
  * def asynchronous(priority: Int)(task: =>Unit): Unit
  * A single worker thread picks tasks submitted to the pool and executes them.
  * Whenever the worker thread picks a new task from the pool for execution,
  * that task must have the highest priority in the pool.
  */
object Ex8 extends App {

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

  class PriorityTaskPool extends Thread {
    import scala.collection.mutable.PriorityQueue

    setDaemon(true)
    setName("Worker")

    private val tasks = new PriorityQueue[Task]()

    def poll(): Task = tasks.synchronized {
      while (tasks.isEmpty)
        tasks.wait()
      tasks.dequeue()
    }

    override def run(): Unit = while (true) {
      val t = poll()
      t.task()
    }

    def asynchronous(priority: Int)(task: => Unit): Unit = tasks.synchronized {
      tasks.enqueue(Task(priority, () => task))
      tasks.notify()
    }

  }

  val taskPool = new PriorityTaskPool

  taskPool.start()

  taskPool.asynchronous(10)(println("Priority 10"))
  taskPool.asynchronous(1)(println("Priority 1"))
  taskPool.asynchronous(90)(println("Priority 90"))
  taskPool.asynchronous(55345)(println("Priority 55345"))


  Thread.sleep(5000)
}