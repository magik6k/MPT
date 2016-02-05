package net.magik6k.mpt.client.profile

import net.magik6k.mpt.client.tabs.Tab
import net.magik6k.mpt.client.util.ResourceManager
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div

object ProfileTab extends Tab {
  override lazy val tag: Tag = {
    val resultContainer = div()
    ResourceManager.get("/tab/profile", res => resultContainer.element.appendChild(Tag(res)))
    resultContainer
  }
  override def title: Tag = "Test"
}
