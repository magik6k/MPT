package net.magik6k.mpt.client

import net.magik6k.mpt.client.profile.ProfileTab
import net.magik6k.mpt.client.repositories.{FileTab, RepositoriesTab}
import org.scalajs.dom.window

object PathParser {
  def apply(): Unit = {

    val filePattern = """\/file\/([^/]+)(.+)""".r

    println(s"parse ${window.location.pathname}")

    window.location.pathname match {
      case "/profile" => ProfileTab.focus()
      case "/repositories" => RepositoriesTab.focus()
      case filePattern(pack, file) => FileTab.focus(pack, file)
      case _ =>
    }
  }
}
