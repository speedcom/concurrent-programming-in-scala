def timed[T](body: =>T): Double = {
  val start = System.nanoTime
  body
  val end = System.nanoTime
  ((end - start) / 1000) / 1000.0
}
