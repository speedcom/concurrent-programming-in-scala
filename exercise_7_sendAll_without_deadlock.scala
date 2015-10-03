
def thread(block: => Unit) = {
  val t = new Thread {
    override def run() = block
  }
  t.start()
  t
}

class Account {
  var x = 0

  def add(xx: Int): Unit = synchronized {
    x += xx
  }

  def all = synchronized { x }

}

def sendAll(accounts: Seq[Account], target: Account): Unit = {

  val accountThreads = accounts.map {
    acc =>
      thread { target.add(acc.all) }
  }
  accountThreads.foreach(_.join())

}

object exercise_7_sendAll_without_deadlock extends App {

  val accounts = (1 to 10).map  { _ => val account = new Account(); account.x = 10; account }
  val target = new Account()

  sendAll(accounts, target)

  println("target.all = " + target.all)
}