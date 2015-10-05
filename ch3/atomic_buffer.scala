import java.util.concurrent.atomic._

class AtomicBuffer[T] {

  val buffer = new AtomicReference[List[T]](Nil)

  def +=(x: T): Unit = {
    val xs = buffer.get
    val nxs = x::xs

    if(!buffer.compareAndSet(xs, nxs))
       +=(x)
  }

}

object atomic_buffer extends App {

  import scala.concurrent._

  def execute(body: =>Unit) = ExecutionContext.global.execute(
    new Runnable { def run() = body }
  )

  val intBuffer = new AtomicBuffer[Int]

  execute { ( 0 until 10).foreach(intBuffer.+= )}
  execute { (10 until 20).foreach(intBuffer.+= )}

  Thread.sleep(1000)

  assert(intBuffer.buffer.get.size == 20)
  println(intBuffer.buffer.get)
}