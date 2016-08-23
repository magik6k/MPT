package net.magik6k.mpt.client

import net.magik6k.mpt.client.profile.Profile
import net.magik6k.mpt.client.tabs.TabManager
import net.magik6k.mpt.client.menu._
import scala.scalajs.js

object App extends js.JSApp {
  def main(): Unit = {
    Profile.init(() => {
      SideBar.initialize()
      TabManager.initialize()
      PathParser()
    })
  }
}
