package net.magik6k.mpt.client.repositories

import com.scalawarrior.scalajs.ace
import net.magik6k.mpt.client.tabs.HistoryTab
import net.magik6k.mpt.client.util.ResourceManager
import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.div

import scala.collection.mutable

object FileTab {
  val open = mutable.HashSet[String]()

  def focus(repo: String, pack: String, file: String): Unit = {
    val t = this(repo, pack, file)
    if(t != null) {
      t.focus()
      open += pack + "&&" + file
    }
  }

  def apply(repo: String, pack: String, file: String): FileTabInner = {
    if(!open.contains(pack + "&&" + file))
      new FileTabInner(repo, pack, file)
    else
      null
  }

  class FileTabInner(repo: String, pack: String, file: String) extends HistoryTab(s"/file/$pack/$file") {
    override lazy val tag: Tag = {
      val resultContainer = div().withClass("tab-full")
      ResourceManager.get("/tab/file", res => {
        resultContainer.element.appendChild(Tag(res, div().withClass("tab-full").element))
        val editorContainer = resultContainer.element.getElementsByClassName("editor")(0)
        import net.magik6k.mpt.client.util.hack.AceHelpers._
        val editor = ace.ace.asInstanceOf[MyAce].edit(editorContainer)
        editor.setTheme("ace/theme/ambiance")
        editor.getSession().setMode("ace/mode/lua")

      })
      resultContainer
    }

    override def title: Tag = pack + ":" + file

    override def onClose() = {
      open -= pack + "&&" + file
      true
    }
  }
}
