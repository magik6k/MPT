package net.magik6k.mpt.client.repositories

import net.magik6k.mpt.client.menu.Menu
import net.magik6k.mpt.client.tabs.HistoryTab
import net.magik6k.mpt.client.util.{REST, ResourceManager}
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.raw.{HTMLInputElement, MouseEvent, HTMLElement}
import org.scalajs.dom.document

import scala.collection.mutable
import scala.scalajs.js

object RepositoryTab {
  val open = mutable.HashSet[String]()

  def focus(name: String): Unit = {
    val t = this(name)
    if(t != null) {
      t.focus()
      open += name
    }
  }

  def apply(name: String): RepositoryTabInner = {
    if(!open.contains(name))
      new RepositoryTabInner(name)
    else
      null
  }

  class RepositoryTabInner(name: String) extends HistoryTab("/repository/" + name) {
    override lazy val tag: Tag = {
      val resultContainer = div()
      ResourceManager.get("/tab/repository", res => {
        resultContainer.element.appendChild(Tag(res))
        document.getElementById("new-package-btn").asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
          val nameField = document.getElementById("new-package-name").asInstanceOf[HTMLInputElement]
          nameField.disabled = true
          val req = js.Dynamic.literal("package" -> nameField.value, "repo" -> name)
          REST.post("/api/v1/packages/add", req.asInstanceOf[js.Dynamic], {
            case true =>
              Menu.addPackage(name, nameField.value)
              nameField.disabled = false
            case false =>
              nameField.value = "ERROR"
              nameField.disabled = false
          })
        }
      })
      resultContainer
    }

    override def title: Tag = name

    override def onClose() = {
      open -= name
      true
    }
  }

}