package ch7

import scala.concurrent.stm._
import scala.concurrent._
import ExecutionContext.Implicits.global

object NodeSTM extends App {

  case class Node(elem: Int, next: Ref[Node]) {
    def append(n: Node) = atomic { implicit txn =>
      val oldNext = next()
      next() = n
      next().next() = oldNext
    }
    def nextNode() = next.single()
  }

  val nodes = Node(1, Ref(Node(4, Ref(Node(6, Ref(null))))))

  val f = Future { nodes.append(Node(3, Ref(null))) }
  val g = Future { nodes.append(Node(10, Ref(null))) }

  for(_ <- f; _ <- g) yield {
    println(s"Next node is: ${nodes.nextNode}");
  }

}