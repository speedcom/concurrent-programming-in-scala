// FILE SYSTEM MANAGER

// Our filesystem API must ensure that:
// • If a thread is creating a new file, then that file cannot be copied or deleted
// • If one or more threads are copying a file, then the file cannot be deleted
// • If a thread is deleting a file, then the file cannot be copied
// • Only a single thread in the file manager is deleting a file at a time


/*
|----------|
| Creating |
|----------|
      |
      v
|----------|       |------------|       |------------|
|   Idle   | <---> | Copying(1) | <---> | Copying(2) | <---> ...
|----------|       |------------|       |------------|
      |
      v
|----------|
| Deleting |
|----------|
*/

sealed trait FileState
class Creating extends FileState
class Idle extends FileState
class Copying(val n: Int) extends FileState
class Deleting extends FileState

/*
* Simulate file/directory
*/
case class Entry(isDir: Boolean) {
  import java.util.concurrent.atomic._

  val state = new AtomicReference[FileState](new Idle)
}

object Entry {

  def prepareForDelete(entry: Entry): Boolean = {
    val s0 = entry.state.get

    s0 match {
      case i: Idle     => {
        if(entry.state.compareAndSet(s0, new Delete))
          true
        else prepareForDelete(entry)
      }
      case cp: Copying => println(s"have to be in Idle mode, not $cp"); false
      case c: Creating => println(s"have to be in Idle mode, not $c "); false
      case d: Deleting => println(s"have to be in Idle mode, not $d "); false
    }
  }

}