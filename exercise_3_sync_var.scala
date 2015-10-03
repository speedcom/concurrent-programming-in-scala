class SyncVar[T] {

  var value: Option[T] = None

  def get(): T = value.synchronized {
    val res = value.get
    value = None
    res
  }

  def put(x: T): Unit = value.synchronized {
    if(value.isEmpty)
      value = Some(x)
    else
      throw new Exception("put on full object")
  }
}