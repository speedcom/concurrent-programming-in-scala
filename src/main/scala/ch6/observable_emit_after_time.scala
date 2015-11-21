package ch6

import rx.lang.scala._
import akka.event.Logging
import learningconcurrency._
import scala.concurrent.duration._

object ObservablesEmitAfterTime extends App {

  val obs = Observable.timer(1 seconds)
  obs.subscribe(_ => log("Timeout"))
  obs.subscribe(_ => log("Next timeout"))
  Thread.sleep(2000)
}