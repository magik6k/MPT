package net.magik6k.mpt.client.tabs

import net.magik6k.mpt.client.util.{smallicon, icon}
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.document
import org.scalajs.dom.raw.BeforeUnloadEvent
import org.scalajs.dom.window

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
    *
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
    *
    * @param tab The tab
    * @return Whether succeed
    */
  def focus(tab: Tab): Boolean = {
    if(!tabs.contains(tab) && !open(tab)) return false
    if(!focused.contains(tab)) {
      if(!(focused.isEmpty || focused.get.onBlur())) return false
      if(!tab.onFocus()) return false
      focused match {
        case Some(t: Tab) =>
          t.tabContent.hide()
        case _ =>
      }
      focused = Option(tab)
      redrawTabList()
      tab.tabContent.show()
    }
    true
  }

  def close(tab: Tab): Boolean = {
    if(!tabs.contains(tab) || !tab.onClose()) return false
    if(tab.closeSafe || window.confirm("Are you sure you want to close unsaved tab?")) {
      tabs -= tab
      container.removeChild(tab.tabContent)
      redrawTabList()
      true
    } else false
  }

  protected def redrawTabList(): Unit = {
    while(switcher.firstChild != null)
      switcher.removeChild(switcher.firstChild)
    tabs.foreach(t => switcher.appendChild({
      val e = div(
        div(t.title).onclick(e => t.focus()),
        div(smallicon("delete85")).onclick(e => t.close())
      ).withClass("tab-title")
      if(focused.contains(t)) e.withClass("tab-focused")
      e
    }))
  }

  window.onbeforeunload = { e: BeforeUnloadEvent =>
    if(tabs.exists(!_.closeSafe))
      "Some tabs may contain unsaved work!"
  }
}
