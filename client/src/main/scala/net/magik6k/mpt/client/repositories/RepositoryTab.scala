package net.magik6k.mpt.client.repositories

import net.magik6k.mpt.client.menu.Menu
import net.magik6k.mpt.client.tabs.HistoryTab
import net.magik6k.mpt.client.util.{icon, REST, ResourceManager}
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags._
import org.scalajs.dom.raw.{HTMLInputElement, MouseEvent, HTMLElement}
import org.scalajs.dom.document

import scala.collection.mutable
import scala.scalajs.js

object RepositoryTab {
  val open = mutable.HashMap[String, RepositoryTabInner]()

  def focus(name: String): Unit = {
    val t = this(name)
    if(t != null) {
      t.focus()
      open += name -> t
    } else open(name).focus()
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

        var getCollaborators: ()=>Unit = null
        getCollaborators = () => {
          REST.get(s"/api/v1/repository/users/$name", res => {
            val elem = resultContainer.element.getElementsByClassName("collaborator-list")(0)
            while(elem.firstChild != null) elem.removeChild(elem.firstChild)
            res.asInstanceOf[js.Array[String]].foreach(user => {
              val delUserElem = button("X").onclick({ e: MouseEvent =>
                val req = js.Dynamic.literal("collaborator" -> user, "repo" -> name)
                REST.post("/api/v1/repository/deluser", req.asInstanceOf[js.Dynamic], {
                  case true =>
                    getCollaborators()
                })
              })
              elem.appendChild(p(delUserElem, user))
            })
          })
        }
        getCollaborators()

        resultContainer.element.getElementsByClassName("new-collaborator-btn")(0).asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
          val nameField = resultContainer.element.getElementsByClassName("new-collaborator-name")(0).asInstanceOf[HTMLInputElement]
          val req = js.Dynamic.literal("collaborator" -> nameField.value, "repo" -> name)
          REST.post("/api/v1/repository/adduser", req.asInstanceOf[js.Dynamic], {
            case true =>
              getCollaborators()
          })
        }

        resultContainer.element.getElementsByClassName("new-package-btn")(0).asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
          val nameField = resultContainer.element.getElementsByClassName("new-package-name")(0).asInstanceOf[HTMLInputElement]
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

    override def title: Tag = span(icon("home153"), name)

    override def onClose() = {
      open -= name
      true
    }
  }

}