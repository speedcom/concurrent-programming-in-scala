package learningconcurrency.exercises

import learningconcurrency.chapters.ch2.thread

/**
1. Implement a parallel method, which takes two computation blocks a
and b, and starts each of them in a new thread. The method must return
a tuple with the result values of both the computations. It should have the
following signature:
def parallel[A, B](a: =>A, b: =>B): (A, B)
  */

object Ex1 extends App {

  def parallel[A, B](a: =>A, b: =>B): (A, B) = {

    var aRes = null.asInstanceOf[A]
    var bRes = null.asInstanceOf[B]

    val t1 = thread {
      aRes = a
    }
    val t2 = thread {
      bRes = b
    }

    t1.join()
    t2.join()

    (aRes, bRes)
  }

}
