import scala.concurrent.{Promise, Future}

implicit class FutureOps[T](self: Future[T]) {

  def or(that: Future[T]): Future[T] = {
    val p = Promise[T]

    self onComplete { f => p tryComplete f }
    that onComplete { f => p tryComplete f }

    p.future
  }

}


