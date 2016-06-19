package learningconcurrency.exercises

import learningconcurrency.chapters.ch2.thread
/**
  * 4. The SyncVar object from the previous exercise can be cumbersome to use,
due to exceptions when the SyncVar object is in an invalid state. Implement
a pair of methods isEmpty and nonEmpty on the SyncVar object. Then,
implement a producer thread that transfers a range of numbers 0 until 15
to the consumer thread that prints them.
  */

class SyncVar[T] {

  var v: Option[T] = None

  def isEmpty = this.synchronized(v.isEmpty)
  def nonEmpty = this.synchronized(v.nonEmpty)

  def get(): T = this.synchronized {
    if(isEmpty) throw new Exception("must be non-empty")
    else {
      val r = v.get
      v = None
      r
    }
  }

  def put(x: T): Unit = this.synchronized {
    if(isEmpty)
      v = Some(x)
    else
      throw new Exception("must be empty")
  }
}

object Ex4 extends App {

  val syncV = new SyncVar[Int]

  val consumer = thread {
//    var i = 0
//    while(i < 15) {

      while({println("again here"); syncV.nonEmpty}) {
//        i += 1
        println(s"[consuming next number]: ${syncV.get()}")
      }
    }

  val producer = thread {
    var i = 0
    while(i < 15) {
      println("i: " + i)
      if(syncV.isEmpty) {
        syncV.put(i)
        println(s"[producing next number]: $i")
        i += 1
      }
    }
  }

  producer.join()
  consumer.join()

}
