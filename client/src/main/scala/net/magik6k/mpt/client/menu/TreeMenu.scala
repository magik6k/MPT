package net.magik6k.mpt.client.menu

import net.magik6k.mpt.client.util.tags.Tags.div

class TreeMenu(elements: Leaf*) {
  def tag = {
    div(
      div(elements.map(t => div(t.tag)): _*).withClass("tree-node")
    ).withClass("tree-root")
  }
}
