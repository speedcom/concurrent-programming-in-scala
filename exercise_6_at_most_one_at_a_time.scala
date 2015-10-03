class SyncQueue[T](n: Int) {
  import scala.collection._

  var queue = immutable.Queue[T]()

  def put(x: T): Unit = synchronized {
    while(queue.size == n)
      this.wait()

    queue = queue :+ x
  }

  def get: T = synchronized {
    while(queue.size < 1)
      this.wait()

    val (head, tail) = queue.dequeue
    queue = tail
    this.notify()
    head
  }
}

def thread(block: => Unit) = {
  val t = new Thread {
    override def run() = block
  }
  t.start()
  t
}

object exercise_6_at_most_one_at_a_time extends App {

  val syncQueue = new SyncQueue[Int](n = 10)

  val producerT = thread {
    var x = 0
    while(x < 15) {
      println(s"producerT put $x")
      syncQueue.put(x)
      x += 1
    }
  }

  val consumerT = thread {
    var x = -1
    while(x < 14) {
      println(s"consumerT get ${syncQueue.get}")
      x += 1
    }
  }

  producerT.join()
  consumerT.join()

}