package ch6

package object learningconcurrency {
  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")
}
