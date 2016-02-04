package net.magik6k.mpt.client

import net.magik6k.mpt.client.tabs.TabManager
import org.scalajs.dom.document
import net.magik6k.mpt.client.menu._
import scala.scalajs.js

object App extends js.JSApp {
  def main(): Unit = {
    val menu = new TreeMenu(
      TreeEntry("Profile"),
      TreeEntry("Search Package"),
      TreeNode("Repositories",
        TreeNode("test-repo",
          TreeNode("some-package",
            TreeNode("[rw] master (some-package:dev)",
              TreeEntry("/etc/some.cfg"),
              TreeEntry("/file.test"),
              TreeEntry("/file1.new")
            ),
            TreeNode("[rw] release (some-package)",
              TreeEntry("/etc/some.cfg"),
              TreeEntry("/file.test"),
              TreeEntry("/file1.new")
            ),
            TreeNode("Snapshots",
              TreeNode("[ro] v1.0",
                TreeEntry("/etc/some.cfg"),
                TreeEntry("/file.test")
              ),
              TreeNode("[ro] v0.1",
                TreeEntry("/file.test")
              )
            )
          )
        )
      )
    )

    document.getElementById("side-menu").appendChild(menu.tag)
    TabManager.initialize()
  }
}
