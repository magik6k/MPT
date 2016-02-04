package pl.starchasers.panel.common.util

object Using {
  def apply[T <: { def close() }, B <: Any]
  (resource: T)
  (block: T => B) : B
  = {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }
}