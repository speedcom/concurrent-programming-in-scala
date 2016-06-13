package learningconcurrency.exercises

import learningconcurrency.chapters.ch2.thread

/**
  * 2. Implement a periodically method, which takes a time interval duration
specified in milliseconds, and a computation block b. The method starts a
thread that executes the computation block b every duration milliseconds.
It should have the following signature:
def periodically(duration: Long)(b: =>Unit): Unit
  */

object Ex2 extends App {

  def periodically(duration: Long)(b: () =>Unit): Unit = {

    val t = thread {
      while(true) {
        b()
        Thread.sleep(duration)
      }
    }
    t.setName("periodically-thread")
    t.setDaemon(true)
    t.start()
  }

}
