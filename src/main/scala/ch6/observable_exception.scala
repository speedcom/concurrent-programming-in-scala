package ch6

import rx.lang.scala._
import akka.event.Logging
import learningconcurrency._

object ObservableExceptions extends App {

  val exc = new RuntimeException()
  val o = Observable.items(1,2) ++ Observable.error(exc)
  o.subscribe(
    x => log(s"number $x"),
    t => log(s"an error occured $t")
  )

  // 5 and 6 will not be emmited
  val o2 = Observable.items(3,4) ++ Observable.error(exc) ++ Observable.items(5,6)
  o2.subscribe(
    x => log(s"number $x"),
    t => log(s"an error occured $t")
  )
}