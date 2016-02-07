package net.magik6k.mpt.client.menu

import net.magik6k.mpt.client.util.smallicon
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.raw.MouseEvent

case class TreeNodeButton(label: Tag, handler: (MouseEvent)=>Unit, elements: Leaf*) extends Leaf {
  def tag = {
    div(
      div(smallicon("calculation"), div(label).onclick(handler).withClass("menu-button")).withClass("leaf"),
      div(elements.map(t => div(t.tag)): _*).withClass("tree-node")
    ).withClass("tree-entry")
  }
}
