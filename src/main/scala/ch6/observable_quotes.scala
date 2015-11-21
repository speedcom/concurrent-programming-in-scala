package ch6

import rx.lang.scala._
import akka.event.Logging
import learningconcurrency._
import scala.concurrent.duration._
import scala.io.Source
import scala.concurrent._
import ExecutionContext.Implicits.global

object ObservableQuotes extends App {

  def fetchQuote(url: String) = Future {
    blocking {
      Source.fromURL(url).getLines().mkString
    }
  }

  def fetchQuoteObservable(url: String) = Observable.from(fetchQuote(url))

  val url = "http://www.iheartquotes.com/api/v1/random?show_permalink=false&show_source=false"

  def quotes: Observable[Observable[String]] = {
    Observable
      .interval(0.5 seconds)
      .take(4)
      .map { nr =>
        fetchQuoteObservable(url).map(txt => s"$nr - $txt")
      }
  }

  quotes.concat.subscribe(log _)

}