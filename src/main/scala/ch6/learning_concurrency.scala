package ch6

object learningconcurrency {
  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")
}
