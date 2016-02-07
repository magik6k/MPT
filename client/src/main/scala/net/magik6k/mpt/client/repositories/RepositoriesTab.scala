package net.magik6k.mpt.client.repositories

import net.magik6k.mpt.client.tabs.{HistoryTab, Tab}
import net.magik6k.mpt.client.util.ResourceManager
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div

object RepositoriesTab extends HistoryTab("/repositories") {
  override lazy val tag: Tag = {
    val resultContainer = div()
    ResourceManager.get("/tab/repositories", res => resultContainer.element.appendChild(Tag(res)))
    resultContainer
  }
  override def title: Tag = "Manage repositories"
}
