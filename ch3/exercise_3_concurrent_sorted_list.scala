/*
1. Under the hood, the ConcurrentSortedList class should use a linked list of
atomic references. Ensure that your implementation is lock-free and avoids
ABA problems.
2. The Iterator object returned by the iterator method must correctly
traverse the elements of the list in the ascending order under the assumption
that there are no concurrent invocations of the add method.
*/


class ConcurrentSortedList[T](implicit val ord: Ordering[T]) {
  import java.util.concurrent.atomic._

  val buffer = new AtomicReference[List[T]](Nil)

  def add(x: T): Unit = {
    val s0 = buffer.get

  }

  def iterator: Iterator[T] = ???
}





