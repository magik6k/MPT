package net.magik6k.mpt.client.menu

import net.magik6k.mpt.client.util.smallicon
import net.magik6k.mpt.client.util.tags.Tags._

case class TreeNode(label: String, elements: Leaf*) extends Leaf {
  def tag = {
    div(
      div(smallicon("calculation"), label).withClass("leaf"),
      div(elements.map(t => div(t.tag)): _*).withClass("tree-node")
    ).withClass("tree-entry")
  }
}
