package learningconcurrency.exercises

import learningconcurrency.chapters.ch2.thread

/**
  * 7. The send method in the Deadlocks section was used to transfer money
between the two accounts. The sendAll method takes a set accounts of bank
accounts and a target bank account, and transfers all the money from every
account in accounts to the target bank account. The sendAll method has
the following signature:
  def sendAll(accounts: Set[Account], target: Account): Unit
Implement the sendAll method and ensure that a deadlock cannot occur.
  */
object Ex7 extends App {

  case class Account(name: String, var money: Int)

  def sendAll(accounts: Set[Account], target: Account): Unit = {
    accounts.foreach { a =>
      a.synchronized {
        target.synchronized {
          target.money += a.money
          a.money = 0
       }
      }
    }
  }

  val accounts = Set(
    Account("name_1", 100),
    Account("name_2", 100),
    Account("name_3", 100),
    Account("name_4", 100),
    Account("name_5", 100),
    Account("name_6", 100),
    Account("name_7", 100),
    Account("name_8", 100),
    Account("name_9", 100),
    Account("name_10", 100)
  )


  val t1 = thread { sendAll(accounts, accounts.head) }

  t1.join()

  assert(accounts.head.name == "name_1")
  assert(accounts.head.money == 100 * 10)
  assert(accounts.tail.forall(_.money == 0))


}
