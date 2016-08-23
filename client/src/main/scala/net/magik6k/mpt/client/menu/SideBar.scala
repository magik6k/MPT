package net.magik6k.mpt.client.menu

import net.magik6k.mpt.client.util.hack.FixedDataTransfer
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom

object SideBar {

  def initialize(): Unit = {
    Menu.createMenu()

    var x: Int = 0
    var dragX: Int = 0

    val win = document.getElementById("side-menu-holder")

    win.asInstanceOf[HTMLElement].ondragstart = {
      (e: dom.DragEvent) => {
        val img = div().css("display" -> "none")
        e.dataTransfer.asInstanceOf[FixedDataTransfer].setDragImage(img, 0, 0)
        e.dataTransfer.effectAllowed = "copy"
        e.dataTransfer.setData("text/plain", "window")
        dragX = e.screenX.toInt - x
      }
    }
    win.asInstanceOf[HTMLElement].ondrag = {
      (e: dom.DragEvent) => {
        x = e.screenX.toInt - dragX
        updatePosition()
      }
    }
    win.asInstanceOf[HTMLElement].ondragend = {
      (e: dom.DragEvent) => {
        x = e.screenX.toInt - dragX
        updatePosition()
      }
    }

    def updatePosition() {
      win.asInstanceOf[HTMLElement].style.marginLeft = Math.min(Math.max(x, -280), 0).toString + "px"
    }
  }
}
