package net.magik6k.mpt.client.tabs

import org.scalajs.dom.document

import scala.collection.immutable.HashSet

object TabManager {
  val container = document.getElementById("tab-container")
  val switcher = document.getElementById("tab-switcher")
  var tabs = HashSet[Tab]()
  var focused: Option[Tab] = Option.empty

  def initialize(): Unit = {

  }

  /**
    * Opens a tab. DOESN'T FOCUS IT!
    * @param tab The tab
    * @return Whether succeed
    */
  def open(tab: Tab): Boolean = {
    if(tabs.contains(tab) || !tab.onOpen()) return false
    tabs += tab
    tab.tabContent.hide()
    container.appendChild(tab.tabContent)
    redrawTabList()
    true
  }

  /**
    * Focus a tab. Open if not opened
    * @param tab The tab
    * @return Whether succeed
    */
  def focus(tab: Tab): Boolean = {
    if(!tabs.contains(tab) && !open(tab)) return false
    if(!focused.contains(tab)) {
      if(!(focused.isEmpty || focused.get.onBlur())) return false
      if(!tab.onFocus()) return false
      if(focused.isDefined) focused.get.tabContent.hide()
      focused = Option(tab)
      tab.tabContent.show()
    }
    true
  }

  protected def redrawTabList(): Unit = {
    while(switcher.firstChild != null)
      switcher.removeChild(switcher.firstChild)
    tabs.foreach(t => switcher.appendChild(t.title))
  }
}
