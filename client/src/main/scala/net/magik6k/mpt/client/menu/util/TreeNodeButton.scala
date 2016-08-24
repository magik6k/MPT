package net.magik6k.mpt.client.menu.util

import net.magik6k.mpt.client.menu.Leaf
import net.magik6k.mpt.client.util.smallicon
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.{a, div}
import org.scalajs.dom.raw.{Node, MouseEvent}

case class TreeNodeButton(label: Tag, handler: (MouseEvent)=>Unit, elements: Leaf*) extends Leaf {
  val nodeDiv = div(elements.map(t => div(t.tag)): _*).withClass("tree-node")
  val toggle = a(smallicon("plus81"))
  private var open = false
  private var opened = false
  private var openHandler: () => Unit = null

  val element = {
    div(
      div(toggle, div(label).onclick(handler).withClass("menu-button")).withClass("leaf"),
      nodeDiv
    ).withClass("tree-entry").withClass("tree-collapsed")
  }
  def tag = element

  toggle.onclick(event => {
    element.toggleClass("tree-collapsed")
    open = !open
    updateIcon()
    if(open && !opened && openHandler != null) {
      opened = true
      openHandler()
    }
  })

  def updateIcon(): Unit = {
    if(open)
      toggle.set(smallicon("calculation"))
    else
      toggle.set(smallicon("plus81"))
  }

  def add(leaf: Leaf) = {
    val n = div(leaf.tag)
    nodeDiv.appendChild(n)
    n
  }

  def remove(child: Node): Unit = {
    nodeDiv.removeChild(child)
  }

  def firstOpen(callback: () => Unit): Unit = {
    openHandler = callback
  }
}
