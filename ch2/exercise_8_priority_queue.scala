def thread(block: => Unit) = {
  val t = new Thread { override def run() = block }
  t.start()
  t
}

import scala.util.Sorting
import java.util.UUID

case class Task(id: UUID, priority: Int, body: () => Unit)
implicit object TaskOrdering extends Ordering[Task] {
  def compare(t1: Task, t2: Task) = t1.priority compare t2.priority
}

class PriorityTaskPool {
  import scala.collection._

  var queue = immutable.Queue[Task]()

  def asynchronous(priority: Int)(task: => Unit): Unit = synchronized {
    queue = queue :+ Task(UUID.randomUUID, priority, () => task)
  }

  def getTaskHighestPriority: Option[Task] = synchronized {
    if(queue.isEmpty)
      None
    else {
      println("queue: " + queue)
      val t = queue.sortBy(_.priority).max
      queue = queue.filterNot(_.id == t.id)
      println("max task: " + t)
      Some(t)
    }
  }
}

object exercise_8_priority_queue extends App {
  import scala.util.Random

  val pool = new PriorityTaskPool

  val producerT = thread {
    var x = 0
    while(x < 15) {
      val randomPriority = Random.nextInt(100)
      pool.asynchronous(priority = randomPriority)(() => println("random priority: " + randomPriority))
      x += 1
    }
  }

  val consumerT = thread {
    var x = -1
    while(x < 14) {
      pool.getTaskHighestPriority
        .map {
          task =>
            println("executed task with priority " + task.priority)
            task.body()
        }.getOrElse(println("no task to execute"))
      x += 1
    }
  }

  producerT.join()
  consumerT.join()
}