import scala.concurrent._

def execute(body: =>Unit) = ExecutionContext.global.execute(
  new Runnable { def run() = body }
)

// CAS - more or less its implemented this way
def compareAndSet(ov: Long, nv: Long): Boolean = {
  this.synchronized {
    if(this.get == ov) {
      this.set(nv)
      true
    } else false
  }
}

import java.util.concurrent.atomic._

def getUniqueId(uid: AtomicLong): Long = {
  val oldId = uid.get
  val newId = oldId + 1

  if(uid.compareAndSet(oldId, newId)) {
    newId
  } else getUniqueId(uid)
}