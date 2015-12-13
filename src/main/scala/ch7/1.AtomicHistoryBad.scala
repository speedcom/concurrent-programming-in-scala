package ch7

import java.util.concurrent.atomic._
import scala.concurrent._
import ExecutionContext.Implicits.global

object AtomicHistoryBad extends App {

  val urls = new AtomicReference[List[String]](Nil)
  val clen = new AtomicInteger(0)

  def addUrl(url: String) {
    import scala.annotation.tailrec
    def append() {
      val oldUrls = urls.get
      val newUrls = url :: oldUrls
      if(!urls.compareAndSet(oldUrls, newUrls)) append()
    }
    append()
    clen.addAndGet(url.length + 1)
  }

  def getUrlArray(): Array[Char] = {
    val array = new Array[Char](clen.get)
    val urlList = urls.get
    for((ch, i) <- urlList.map(_ + "\n").flatten.zipWithIndex) {
      array(i) = ch
    }
    array
  }

  Future {
    try { println(s"sending: ${getUrlArray().mkString}") }
    catch { case e: Exception => println(s"Houston... $e")}
  }

  Future {
    addUrl("http://scala-lang.org")
    addUrl("http://onet.pl")
    addUrl("http://pclab.pl")
    println("done browsing")
  }
  Thread.sleep(1000)
}