package net.magik6k.mpt.client.menu

import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.raw.{MouseEvent, HTMLElement}

case class TreeButton(label: Tag, handler: ()=>Unit) extends Leaf {
  val element = div(label).withClass("menu-button")
  element.getNode.asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
    if(e.button == 0) handler()
  }

  override def tag: Tag = element
}