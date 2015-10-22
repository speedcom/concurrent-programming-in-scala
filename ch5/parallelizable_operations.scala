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

object ParSizeEffectsIncorrect extends App {

  def intersectionSize(a: GenSet[Int], b: GenSet[Int]): Int = {
    var total = 0

    for(x <- a)
      if(b contains x)
        total += 1

    total
  }

  val a = (0 to 1000).toSet
  val b = (0 to 1000 by 4).toSet

  val seqres = intersectionSize(a, b)
  val parres = intersectionSize(a.par, b.par)
  println(s"Sequential result - $seqres")
  println(s"Parallel result - $parres")
}
