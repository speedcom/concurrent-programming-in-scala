import akka.actor.{Actor, ActorSystem, ActorLogging, Props, ActorRef}
import com.typesafe.config._
import scala.collection._
import akka.util.Timeout
import akka.pattern.{ask, pipe, gracefulStop}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

object Remoting {
  def remotingConfig(port: Int) = ConfigFactory.parseString(s"""
    akka {
      actor.provider = "akka.remote.RemoteActorRefProvider"
      remote {
        enabled-transports = ["akka.remote.netty.tcp"]
        netty.tcp {
          hostname = "127.0.0.1"
          port = $port
        }
      }
    }
    """
  )
  def remotingSystem(name: String, port: Int): ActorSystem =
    ActorSystem(name, remotingConfig(port))
}

class Pongy extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "ping" =>
      log.info("Got a ping -- ponging back!")
      sender ! "pong"
      context.stop(self)
  }
  override def postStop() = log.info("pongy going down")
}


class Pingy extends Actor {
  def receive = {
    case pongyRef: ActorRef =>
      implicit val timeout = Timeout(2 seconds)
      val future = pongyRef ? "ping"
      pipe(future) to sender
  }
}

class Runner extends Actor {
  val pingy = context.actorOf(Props[Pingy], "pingy")
  def receive = {
    case "start" =>
      val path = context.actorSelection("akka.tcp://PongyDimension@127.0.0.1:24321/user/pongy")
      path ! Identify(0)
    case ActorIdentity(0, Some(ref)) =>
      pingy ! ref
    case ActorIdentity(0, None) =>
      println("Something's wrong -- no pongy anywhere!")
      context.stop(self)
    case "pong" =>
      println("got a pong from another dimension.")
      context.stop(self)
  }
}

object RemotingPongySystem extends App {
  val system = Remoting.remotingSystem("PongyDimension", 24321)
  val pongy = system.actorOf(Props[Pongy], "pongy")
  Thread.sleep(15000)
  system.shutdown()
}

object RemotingPingySystem extends App {
  val system = Remoting.remotingSystem("PingyDimension", 24567)
  val runner = system.actorOf(Props[Runner], "runner")
  runner ! "start"
  Thread.sleep(5000)
  system.shutdown()
}

