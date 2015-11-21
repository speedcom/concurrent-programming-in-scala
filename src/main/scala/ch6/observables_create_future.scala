package ch6

import rx.lang.scala._
import akka.event.Logging
import learningconcurrency._

import scala.concurrent._
import ExecutionContext.Implicits.global

object ObservablesCreateFuture extends App {

  // val f = Future { "Back to the Future(s) "}
  // val o = Observable.create[String] { obs =>
  //   f       .foreach { case s => obs.onNext(s); obs.onCompleted(); }
  //   f.failed.foreach { case e => obs.onError(e) }
  //   Subscripiton()
  // }
  // o.subscribe(log _)
}