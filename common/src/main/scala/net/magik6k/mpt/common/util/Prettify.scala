package net.magik6k.mpt.common.util

object Prettify {
  def apply(bytes: Double): String = {
    if(bytes < 1024) return bytes + "B"
    val exp = (Math.log(bytes) / Math.log(1024)).toInt
    val pre = "KMGTPE".charAt(exp)
    "%.1f %siB".format(bytes / Math.pow(1024, exp), pre)
  }
}