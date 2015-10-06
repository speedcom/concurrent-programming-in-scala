import akka.actor.{Actor, ActorLogging, Props}
import akka.event.Logging

class ExceptionProneActor extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case s: String => println(s)
    case msg => throw new RuntimeException
  }

  override def postRestart(t: Throwable) = println("OverrideSuperivisionStrategy restarted")
}

object ExceptionProneActor {
  def props = Props[ExceptionProneActor]
}

class SupervisorActor extends Actor {
  import akka.actor.OneForOneStrategy
  import akka.actor.SupervisorStrategy._
  import akka.actor.ActorKilledException

  override def supervisorStrategy = OneForOneStrategy() {
    case ake: ActorKilledException => Restart
    case _ => Escalate
  }

  def receive = PartialFunction.empty

  val child = context.actorOf(ExceptionProneActor.props, name = "naughty")
}

object SupervisorActor {
  def props = Props[SupervisorActor]
}

object naughy_actor_override_supervision_strategy extends App {
  import akka.actor.ActorSystem
  import akka.actor.Kill

  val actorSystem = ActorSystem("supervision-strategy-system")
  val supervisorActor = actorSystem.actorOf(SupervisorActor.props, "super")

  actorSystem.actorSelection("/user/super/*") ! Kill
  actorSystem.actorSelection("/user/super/*") ! "that message is handled by restarted actor"
}