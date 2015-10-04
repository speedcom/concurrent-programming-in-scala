class SyncVar[T] {

  var value: Option[T] = None

  def isEmpty = value.synchronized { value.isEmpty }
  def nonEmpty = value.synchronized { !value.isEmpty }

  def getWait(): T = this.synchronized {
    if(isEmpty)
      this.wait()

    val res = value.get
    this.value = None
    this.notify()
    res
  }

  def putWait(x: T): Unit = this.synchronized {
    if(nonEmpty)
      this.wait()

    this.value = Some(x)
    this.notify()
  }
}

def thread(block: => Unit) = {
  val t = new Thread {
    override def run() = block
  }
  t.start()
  t
}

object exercise_5_sync_var_non_busy_waiting extends App {

  val intSyncVar = new SyncVar[Int]

  val producerT = thread {
    var x = 0
    while(x < 15) {
      println(s"producerT put $x")
      intSyncVar.putWait(x)
      x += 1
    }
  }

  val consumerT = thread {
    var x = -1
    while(x < 14) {
      println(s"consumerT get ${intSyncVar.getWait}")
      x += 1
    }
  }

  producerT.join()
  consumerT.join()
}
