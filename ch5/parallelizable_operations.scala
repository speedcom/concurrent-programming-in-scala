def timed[T](body: =>T): Double = {
  val start = System.nanoTime
  body
  val end = System.nanoTime
  ((end - start) / 1000) / 1000.0
}

object ParNonParallelizableCollections extends App {
  val list = List.fill(1000000)("")
  val vector = Vector.fill(1000000)("")
  println(s"list conversion time: ${timed(list.par)} ms")
  println(s"vector conversion time: ${timed(vector.par)} ms")
}
