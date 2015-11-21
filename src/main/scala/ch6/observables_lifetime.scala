package ch6

import rx.lang.scala._
import akka.event.Logging
import learningconcurrency._

object ObservablesLifetime extends App {

  val classics = List("Die hard", "Titanic", "Good, bad, ugly")
  val obs = Observable.from(classics)
  obs.subscribe(new Observer[String] {
    override def onNext(event: String): Unit = log(s"Movies Watchlist - $event")
    override def onError(e: Throwable): Unit = log(s"Ooops - $e!")
    override def onCompleted(): Unit = log(s"No more movies.")
  })

}