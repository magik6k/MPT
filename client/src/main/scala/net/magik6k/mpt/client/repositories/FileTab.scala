package net.magik6k.mpt.client.repositories

import com.scalawarrior.scalajs.ace
import com.scalawarrior.scalajs.ace.EditorCommand
import net.magik6k.mpt.client.menu.Menu
import net.magik6k.mpt.client.tabs.HistoryTab
import net.magik6k.mpt.client.util.{REST, ResourceManager}
import net.magik6k.mpt.client.util.tags.{BasicTag, Tag}
import net.magik6k.mpt.client.util.tags.Tags.div
import org.scalajs.dom.raw.{MouseEvent, HTMLElement}

import scala.collection.mutable
import scala.scalajs.js

object FileTab {
  val open = mutable.HashSet[String]()

  def focus(pack: String, file: String): Unit = {
    val t = this(pack, file)
    if(t != null) {
      t.focus()
      open += pack + "&&" + file
    }
  }

  def apply(pack: String, file: String): FileTabInner = {
    if(!open.contains(pack + "&&" + file))
      new FileTabInner(pack, file)
    else
      null
  }

  class FileTabInner(pack: String, file: String) extends HistoryTab(s"/file/$pack$file") {
    override lazy val tag: Tag = {
      val resultContainer = div().withClass("tab-full")
      ResourceManager.get("/tab/file", res => {
        resultContainer.element.appendChild(Tag(res, div().withClass("tab-full").element))
        setupEditor(resultContainer)
      })
      resultContainer
    }

    private def setupEditor(tag: BasicTag): Unit = {
      val editorContainer = tag.element.getElementsByClassName("editor")(0)
      import net.magik6k.mpt.client.util.hack.AceHelpers._
      ResourceManager.get(s"/api/file/$pack$file", text => {
        editorContainer.asInstanceOf[HTMLElement].innerHTML = text
        val editor = ace.ace.asInstanceOf[MyAce].edit(editorContainer)
        editor.setTheme("ace/theme/ambiance")
        editor.getSession().setMode("ace/mode/lua")

        tag.element.getElementsByClassName("editor-save")(0).asInstanceOf[HTMLElement].onclick = { e: MouseEvent =>
          save(editor.getSession().getDocument().getValue())
        }

        editor.commands.addCommand(js.Dynamic.literal(
          "name" -> "save",
          "bindKey" -> js.Dynamic.literal("win" -> "Ctrl-S", "mac" -> "Command-S"),
          "exec" -> { () => { save(editor.getSession().getDocument().getValue()) } }
        ).asInstanceOf[EditorCommand])

      })
    }

    def save(data: String): Unit = {
      val req = js.Dynamic.literal("file" -> file, "content" -> data)
      REST.post("/api/v1/package/" + pack + "/file/save", req.asInstanceOf[js.Dynamic], {
        case true =>

        case false =>

      })
    }

    override def title: Tag = pack + ":" + file

    override def closeSafe = false

    override def onClose() = {
      open -= pack + "&&" + file
      true
    }
  }
}