object Executor {
  def execute(body: =>Unit) = ExecutionContext.global.execute(
    new Runnable { def run() = body }
  )
}

object CollectionsConcurrentMapBulk extends App {
  import java.util.concurrent.ConcurrentHashMap
  import scala.collection.JavaConversions._
  import scala.concurrent._

  val names = new ConcurrentHashMap[String, Int]()
  names("Johny") = 0;
  names("Jane")  = 0;
  names("Jack")  = 0;

  Executor.execute { for (n <- 0 until 10) names(s"John $n") = n }
  Executor.execute { for (n <- names) println(s"name: $n")}
}

object CollectionsTrieMapBulk extends App {
  import scala.collection.concurrent._

  val names = new TrieMap[String, Int]
  names("Johny") = 0;
  names("Jane")  = 0;
  names("Jack")  = 0;

  Executor.execute {for (n <- 10 until 100) names(s"John $n") = n}
  Executor.execute {
    println("snapshot time!")
    for (n <- names.map(_._1).toSeq.sorted) println(s"name: $n")
  }
}