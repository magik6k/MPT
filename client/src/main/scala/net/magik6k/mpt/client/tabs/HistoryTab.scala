package net.magik6k.mpt.client.tabs

import org.scalajs.dom.window

abstract class HistoryTab(address: String) extends Tab {
  override final def onFocus(): Boolean = {
    window.history.pushState(null, null, address)
    true
  }
}
