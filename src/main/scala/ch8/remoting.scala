import akka.actor.{Actor, ActorLogging, Props}
import akka.actor.ActorSystem
import com.typesafe.config._
import scala.collection._

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

