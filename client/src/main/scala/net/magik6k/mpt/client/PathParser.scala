package net.magik6k.mpt.client

import net.magik6k.mpt.client.profile.ProfileTab
import net.magik6k.mpt.client.repositories.RepositoriesTab
import org.scalajs.dom.window

object PathParser {
  def apply(): Unit = {
    window.location.pathname match {
      case "/profile" => ProfileTab.focus()
      case "/repositories" => RepositoriesTab.focus()
      case _ =>
    }
  }
}
