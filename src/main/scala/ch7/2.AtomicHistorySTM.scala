package ch7

import scala.concurrent.stm._
import scala.concurrent._
import ExecutionContext.Implicits.global

object AtomicHistorySTM extends App {

  val urls = Ref[List[String]](Nil)
  val clen = Ref(0)

  def addUrl(url: String): Unit = atomic { implicit tx =>
    urls() = url :: urls()
    clen() = clen() + url.length + 1
  }

  def getUrlArray(): Array[Char] = atomic { implicit tx =>
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