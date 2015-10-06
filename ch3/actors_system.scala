trait Actor {
  type React[In, Out] = PartialFunction[In, Out]
  def react: React[Any, Unit]
}

case class ActorRef private (underlying: Actor)

class Mailbox(actorRef: ActorRef) {
  var messages = List.empty[Any]

  def +=(msg: Any): Unit = messages = messages :+ msg

  def get: Option[Any] =
    if(messages.isEmpty)
      None
    else {
      val (msg :: tail) = messages
      messages = tail
      Some(msg)
    }
}

trait Executor {
  import scala.concurrent._

  def execute(body: => Unit) = ExecutionContext.global.execute(
    new Runnable { def run() = body }
  )
}

/*
responsible for giving processor's time to Actors that messages need to be processed
every message from Actors are processed at most one a time (a lot of different Actors can concurrently process messages
but at a time particular Actor can process only one message (that is why mailbox doesn't have to be synchronized))
*/
class Dispatcher {
  import java.util.concurrent.atomic._

  private val executor: Executor = new Executor {}

  val actors = new AtomicReference[List[ActorRef]](Nil) // todo: powinno tutaj to byc???
  val mailboxes = new AtomicReference[List[Mailbox]](Nil)

  @tailrec
  def addActor(actorRef: ActorRef): Unit = {
    val list = mailbox.get
    val newList = list += actorRef
    if(!mailbox.compareAndSet(list, newList))
      +=(actorRef)
  }

  @tailrec
  def addMailbox(m: Mailbox): Unit = ???

  @tailrec
  def run(): Unit = {

    // select mailbox
    // take msg from Actor
    // make sure that Actor not already process another msg

    run()
  }

}

/*
- creates actors and references to it
*/
class ActorSystem {
  val dispatcher = new Dispatcher

  // todo: need to be implemented
  // need to create new mailbox for Actor
  def actorOf: ActorRef = ???

  dispatcher.run()
}


