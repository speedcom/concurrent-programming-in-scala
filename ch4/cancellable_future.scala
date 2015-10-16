import scala.concurrent.{Promise, Future}


object PromiseCancellation extends App {

  class CancellationException extends RuntimeException

  type Cancellable[T] = (Promise[Unit], Future[T])

  def cancellable[T](b: Future[Unit] => T): Cancellable[T] = {
    val cancelPromise = Promise[Unit]
    val cancelFuture = Future {
      val result = b(cancelPromise.future)
      if(!cancelPromise.tryFailure(new RuntimeException))
        throw new CancellationException
      result
    }

    (cancelPromise, cancelFuture)
  }
}

