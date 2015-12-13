package ch7

import scala.concurrent.stm._
import scala.concurrent._
import ExecutionContext.Implicits.global

object CompositionSideEffects extends App {

  val myValue = Ref(0)

  def inc() = atomic { implicit txn =>
    myValue() = myValue() + 1
    println(s"Incementing myValue: " + myValue())
  }

  Future { inc() }
  Future { inc() }
  Future { inc() }

  Thread.sleep(1000)
}