import akka.actor.{Actor, ActorLogging, Props}
import akka.actor.ActorSystem
import com.typesafe.config._
import scala.collection._

class ParentActor extends Actor {
  def receive = {
    case "create" => context.actorOf(ChildActor.props); println("created a new kid")
    case "sayhi"  => println("Kids, say hi!"); for(child <- context.children) child ! "sayhi"
    case "stop"   => println("parent stopping"); context.stop(self)
  }
  override def postStop() = {
    println("parent stopped!")
  }
}
object ParentActor {
  def props = Props[ParentActor]
}

class ChildActor extends Actor {
  def receive = {
    case "sayhi" =>
      val parent = context.parent
      println(s"my parent $parent said hi to me!")
  }

  override def postStop() = {
    println("child stopped!")
  }
}

object ChildActor {
  def props = Props[ChildActor]
}

object parent_child extends App {

  val actorSystem = ActorSystem("exemplary-system")

  val parentActorRef = actorSystem.actorOf(ParentActor.props)

  parentActorRef ! "create"
  parentActorRef ! "create"
  parentActorRef ! "create"
  Thread.sleep(1000)

  parentActorRef ! "sayhi"
  Thread.sleep(1000)

  parentActorRef ! "stop"
  Thread.sleep(3000)

  actorSystem.shutdown()
}