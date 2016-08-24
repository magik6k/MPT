package net.magik6k.mpt.client.repositories

import net.magik6k.mpt.client.menu.Menu
import net.magik6k.mpt.client.tabs.HistoryTab
import net.magik6k.mpt.client.util.{icon, REST, ResourceManager}
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.{span, p, button, div}
import org.scalajs.dom.raw.{HTMLInputElement, MouseEvent, HTMLElement}
import org.scalajs.dom.document
import org.scalajs.dom.window

import scala.collection.mutable
import scala.scalajs.js

object PackageTab {
  val open = mutable.HashMap[String, PackageTabInner]()

  def focus(name: String, repo: String): Unit = {
    val t = this(name, repo)
    if(t != null) {
      t.focus()
      open += name -> t
    } else open(name).focus()
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

        var getDependencies: ()=>Unit = null
        getDependencies = () => {
          REST.get(s"/api/v1/package/$name/dep/list", res => {
            val elem = resultContainer.element.getElementsByClassName("package-dependencies")(0)
            while(elem.firstChild != null) elem.removeChild(elem.firstChild)
            res.asInstanceOf[js.Array[String]].foreach(pack => {

              val delDepElem = button("X").onclick({ e: MouseEvent =>
                val req = js.Dynamic.literal("package" -> pack)
                REST.post(s"/api/v1/package/$name/dep/delete", req.asInstanceOf[js.Dynamic], {
                  case true =>
                    getDependencies()
                })
              })
              elem.appendChild(p(delDepElem, pack))

            })
          })
        }
        getDependencies()

        var getFiles: ()=>Unit = null
        getFiles = () => {
          REST.get(s"/api/v1/package/$name/files", res => {
            val elem = resultContainer.element.getElementsByClassName("package-files")(0)
            while(elem.firstChild != null) elem.removeChild(elem.firstChild)
            res.asInstanceOf[js.Array[String]].foreach(file => {
              val delDepElem = button("X").onclick({ e: MouseEvent =>
                if(window.confirm("Are you sure you want to delete this file?")) {
                  val req = js.Dynamic.literal("file" -> file)
                  REST.post(s"/api/v1/package/$name/file/delete", req.asInstanceOf[js.Dynamic], {
                    case true =>
                      getFiles()
                      Menu.delFile(repo, name, file)
                      FileTab.open.get(name + "&&" + file).foreach(_.close(true))
                  })
                }
              })
              elem.appendChild(p(delDepElem, file))
            })
          })
        }
        getFiles()

        resultContainer.element.getElementsByClassName("new-dependency-btn")(0).asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
          val nameField = resultContainer.element.getElementsByClassName("new-dependency-name")(0).asInstanceOf[HTMLInputElement]
          val req = js.Dynamic.literal("package" -> nameField.value)
          REST.post(s"/api/v1/package/$name/dep/add", req.asInstanceOf[js.Dynamic], {
            case true =>
              getDependencies()
          })
        }

        resultContainer.element.getElementsByClassName("new-file-btn")(0).asInstanceOf[HTMLElement].onclick = (e: MouseEvent) => {
          val nameField = resultContainer.element.getElementsByClassName("new-file-name")(0).asInstanceOf[HTMLInputElement]
          nameField.disabled = true
          if(!nameField.value.startsWith("/"))
            nameField.value = "/" + nameField.value
          val req = js.Dynamic.literal("file" -> nameField.value)
          REST.post("/api/v1/package/" + name + "/file/create", req.asInstanceOf[js.Dynamic], {
            case true =>
              getFiles()
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

    override def title: Tag = span(icon("folder232"), name)

    override def onClose() = {
      open -= name
      true
    }
  }

}
