package net.magik6k.mpt.client

import net.magik6k.mpt.client.profile.ProfileTab
import net.magik6k.mpt.client.repositories.RepositoriesTab
import net.magik6k.mpt.client.tabs.TabManager
import net.magik6k.mpt.client.util.icon
import net.magik6k.mpt.client.util.tags.Tags.span
import org.scalajs.dom.document
import org.scalajs.dom.window
import net.magik6k.mpt.client.menu._
import scala.scalajs.js

object App extends js.JSApp {
  def main(): Unit = {
    val menu = new TreeMenu(
      TreeButton(span(icon("left224"), "Logout"), e => window.location.href = "/logout"),
      TreeButton(span(icon("users81"), "Profile"), e => ProfileTab.focus()),
      TreeEntry(span(icon("magnifying47"), "Search Package")),
      TreeNodeButton("Repositories", e => RepositoriesTab.focus(),
        TreeNode("test-repo",
          TreeNode("some-package",
            TreeNode("[rw] master (some-package:dev)",
              TreeNode("etc", TreeEntry(span(icon("file129"),"some.cfg"))),
              TreeEntry(span(icon("file129"),"/file.test")),
              TreeEntry(span(icon("file129"),"/file1.new"))
            ),
            TreeNode("[rw] release (some-package)",
              TreeNode("etc", TreeEntry(span(icon("file129"),"some.cfg"))),
              TreeEntry(span(icon("file129"),"/file.test")),
              TreeEntry(span(icon("file129"),"/file1.new"))
            ),
            TreeNode("Snapshots",
              TreeNode("[ro] v1.0",
                TreeNode("etc", TreeEntry(span(icon("file129"),"some.cfg"))),
                TreeEntry(span(icon("file129"),"/file.test"))
              ),
              TreeNode("[ro] v0.1",
                TreeEntry(span(icon("file129"),"/file.test"))
              )
            )
          )
        )
      )
    )

    document.getElementById("side-menu").appendChild(menu.tag)
    TabManager.initialize()
    PathParser()
  }
}
