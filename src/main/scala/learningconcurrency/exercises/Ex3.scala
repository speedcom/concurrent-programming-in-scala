package learningconcurrency.exercises

/**
  * 3. Implement a SyncVar class with the following interface:
class SyncVar[T] {
def get(): T = ???
def put(x: T): Unit = ???
}
A SyncVar object is used to exchange values between two or more threads.
When created, the SyncVar object is empty:
°° Calling get throws an exception
°° Calling put adds a value to the SyncVar object
After a value is added to a SyncVar object, we can say that it is non-empty:
°° Calling get returns the current value, and changes the state to empty
°° Calling put throws an exception
  */

object Ex3 extends App {

  class SyncVar[T] {

    var value: Option[T] = None

    def get(): T = value.synchronized {
      value match {
        case Some(v) => value = None; v
        case None    => throw new Exception("SyncVar has no value to get")
      }
    }

    def put(x: T): Unit = value.synchronized {
      value match {
        case Some(v) => throw new Exception("SyncVar is already fulfilled")
        case None    => value = Some(x)
      }
    }
  }



}
