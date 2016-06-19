package learningconcurrency.exercises

import learningconcurrency.chapters.ch2._

import scala.collection.mutable

/**
  * 6. A SyncVar object can hold at most one value at a time. Implement a
*SyncQueue class, which has the same interface as the SyncVar class, but can
*hold at most n values. The parameter n is specified in the constructor of the
*SyncQueue class.
  */

object Ex6 extends App {

  class SyncVar[T](n: Int) {

    private val tasks = mutable.Queue[T]()

    def getWait: T = this.synchronized {
      while(tasks.isEmpty) this.wait()
      val x = tasks.dequeue()
      this.notify()
      x
    }

    def putWait(x: T): Unit = this.synchronized {
      while(tasks.size > n) this.wait()
      tasks.enqueue(x)
      this.notify()
    }

  }

  val syncVar = new SyncVar[Int](5)

  val producer = thread {
    var x = 0
    while(x < 15) {
      syncVar.putWait(x)
      x += 1
    }
  }

  val consumer = thread {
    var x = -1
    while(x < 14) {
      x = syncVar.getWait
      log(s"get: $x")
    }
  }

  producer.join()
  consumer.join()

}
