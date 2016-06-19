package learningconcurrency.exercises

import learningconcurrency.chapters.ch2._

/**
  * 5. Using the isEmpty and nonEmpty pair of methods from the previous exercise (ex4)
requires busy-waiting. Add the following methods to the SyncVar class:
def getWait(): T
def putWait(x: T): Unit
These methods have similar semantics as before, but go into the waiting
state instead of throwing an exception, and return once the SyncVar object
is empty or non-empty, respectively.
  */

object Ex5 extends App {

  class SyncVar[T] {
    var v: Option[T] = None

    def getWait(): T = this.synchronized {
      while(v.isEmpty) this.wait()
      val x = v.get
      v = None
      this.notify()
      x
    }

    def putWait(x: T): Unit = this.synchronized {
      while(v.nonEmpty) this.wait()
      v = Some(x)
      this.notify()
    }

  }

  val syncVar = new SyncVar[Int]()

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
      x = syncVar.getWait()
      log(s"get: $x")
    }
  }

  producer.join()
  consumer.join()
}
