object Executor {
  def execute(body: =>Unit) = ExecutionContext.global.execute(
    new Runnable { def run() = body }
  )
}

class TreiberStack[T] {
  import java.util.concurrent.atomic._

  sealed trait Stack
  case object Empty extends Stack
  case class Node(head: T, tail: Stack) extends Stack

  val stack = new AtomicReference[Stack](Empty)

  def push(x: T): Unit = {
    val s0 = stack.get
    val nstack = new Node(x, s0)
    if(!stack.compareAndSet(s0, nstack))
      push(x)
  }

  def pop(): T = {
    val s0 = stack.get
    s0 match {
      case Node(h, nstack) =>
        if(!stack.compareAndSet(s0, nstack)) {
          println("wrong comparison, trying again pop")
          pop()
        }
        else h
      case Empty => println("empty stack, try later"); pop()
    }
  }
}

object exercise_1_treiber_stack extends App {

  val intTreiberStack = new TreiberStack[Int]

  Executor.execute { (0 until 10) foreach { intTreiberStack.push } }

  (0 until 10) foreach { _ => println(intTreiberStack.pop) }

  Thread.sleep(1000)
}