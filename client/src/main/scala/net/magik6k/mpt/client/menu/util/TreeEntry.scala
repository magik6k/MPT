package net.magik6k.mpt.client.menu.util

import net.magik6k.mpt.client.menu.Leaf
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div

case class TreeEntry(label: Tag) extends Leaf {
  override def tag: Tag = div(label)
}
