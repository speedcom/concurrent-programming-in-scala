package ch6

import rx.lang.scala._
import akka.event.Logging

object ObservablesItems extends App {
  val o = Observable.items("Pascal", "Java", "Scala")
  o.subscribe(name => log(s"learned the $name language"))
  o.subscribe(name => log(s"forgot the $name language"))
}

