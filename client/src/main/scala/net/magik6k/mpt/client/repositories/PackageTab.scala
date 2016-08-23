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

object PackageTab {
  val open = mutable.HashSet[String]()

  def focus(name: String, repo: String): Unit = {
    val t = this(name, repo)
    if(t != null) {
      t.focus()
      open += name
    }
  }

  def apply(name: String, repo: String): PackageTabInner = {
    if(!open.contains(name))
      new PackageTabInner(name, repo)
    else
      null
  }

  class PackageTabInner(name: String, repo: String) extends HistoryTab("/package/" + name) {
    override lazy val tag: Tag = {
      val resultContainer = div()
      ResourceManager.get("/tab/package", res => {
        resultContainer.element.appendChild(Tag(res))
        document.getElementById("new-file-btn").asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
          val nameField = document.getElementById("new-file-name").asInstanceOf[HTMLInputElement]
          nameField.disabled = true
          val req = js.Dynamic.literal("file" -> nameField.value)
          REST.post("/api/v1/package/" + name + "/file/create", req.asInstanceOf[js.Dynamic], {
            case true =>
              Menu.addFile(repo, name, nameField.value)
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
