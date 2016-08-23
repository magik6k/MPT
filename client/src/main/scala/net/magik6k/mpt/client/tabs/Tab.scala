package net.magik6k.mpt.client.tabs

import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div

trait Tab {
  final private[tabs] lazy val tabContent = div(tag).withClass("tab-full")

  def tag: Tag
  def onOpen(): Boolean = true
  def onFocus(): Boolean = true
  def onBlur(): Boolean = true
  def onClose(): Boolean = true

  def title: Tag

  def focus(): Boolean = TabManager.focus(this)
  def close(): Boolean = TabManager.close(this)
}
