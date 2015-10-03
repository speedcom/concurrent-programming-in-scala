class SyncVar[T] {

  var value: Option[T] = None

  def get(): T = this.synchronized {
    val res = value.get
    value = None
    res
  }

  def put(x: T): Unit = this.synchronized {
    if(value.isEmpty)
      value = Some(x)
    else
      throw new Exception("put on full object")
  }

  def isEmpty = synchronized { value.isEmpty }
  def nonEmpty = synchronized { !value.isEmpty }

}

object Exc4 extends App {

  val intSyncVar = new SyncVar[Int]

  val producerT = new Thread {
    override def run(): Unit = (0 until 15) foreach { value =>
      if(intSyncVar.isEmpty)
        intSyncVar.put(value)
      else
        println("producerT: value is NOT empty but SHOULD")
    }
  }

  val consumerT = new Thread {
    override def run(): Unit = while(true) {
      if(intSyncVar.nonEmpty)
        println(intSyncVar.get)
      else
        Thread.sleep(300)
        println(s"consumerT: ${intSyncVar.nonEmpty}")
    }
  }

  println("Starting producer and consumer threads... ")
  producerT.start(); consumerT.start();
  producerT.join(); consumerT.join();

  println("Main thread")
}


