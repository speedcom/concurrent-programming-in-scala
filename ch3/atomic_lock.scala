object atomic_lock extends App {

  import scala.concurrent._
  import java.util.concurrent.atomic._

  def execute(body: =>Unit) = ExecutionContext.global.execute(
    new Runnable { def run() = body }
  )
  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")

  val lock = new AtomicBoolean(false)

  def synchronizedMine(body: => Unit): Unit = {
    while(!lock.compareAndSet(false, true)) {}
    try {
      body
    } finally {
      lock.set(false)
    }
  }
  var count = 0

  (0 until 10).foreach(_ =>  execute { synchronizedMine { count += 1 } })
  Thread.sleep(1000)
  log(s"Count is $count")
}