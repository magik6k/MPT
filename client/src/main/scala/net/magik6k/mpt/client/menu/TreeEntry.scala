package net.magik6k.mpt.client.menu

import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div

case class TreeEntry(label: String) extends Leaf {
  override def tag: Tag = div(label)
}
