package net.magik6k.mpt.client.tabs

import org.scalajs.dom.document
import net.magik6k.mpt.client.util.tags.Tags._

object TabManager {
  val container = document.getElementById("tab-container")
  def initialize(): Unit = {
    container.appendChild(span("aa"))
  }
}
