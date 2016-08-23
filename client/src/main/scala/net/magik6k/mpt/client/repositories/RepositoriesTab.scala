package net.magik6k.mpt.client.repositories

import net.magik6k.mpt.client.menu.Menu
import net.magik6k.mpt.client.tabs.{HistoryTab, Tab}
import net.magik6k.mpt.client.util.{REST, ResourceManager}
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.document
import org.scalajs.dom.raw.{HTMLInputElement, MouseEvent, HTMLElement}

import scala.scalajs.js

object RepositoriesTab extends HistoryTab("/repositories") {
  override lazy val tag: Tag = {
    val resultContainer = div()
    ResourceManager.get("/tab/repositories", res => {
      resultContainer.element.appendChild(Tag(res))
      document.getElementById("new-repo-btn").asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
        val nameField = document.getElementById("new-repo-name").asInstanceOf[HTMLInputElement]
        nameField.disabled = true
        REST.post("/api/v1/repository/add", nameField.value.asInstanceOf[js.Dynamic], {
          case true =>
            Menu.addRepo(nameField.value)
            nameField.disabled = false
          case false =>
            nameField.value = "ERROR"
            nameField.disabled = false
        })
      }
    })
    resultContainer
  }
  override def title: Tag = "Repositories"
}
