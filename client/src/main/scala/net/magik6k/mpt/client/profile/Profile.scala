package net.magik6k.mpt.client.profile

import net.magik6k.mpt.client.util.REST

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

object Profile {
  private var _name: String = null

  def init(ready: ()=>Unit) {
    REST.get("/api/v1/profile", p => {
      _name = p.asInstanceOf[Profile].name
      ready()
    })
  }

  def name = _name

  @js.native
  @JSName("Profile")
  private class Profile extends js.Object {
    val name: String = js.native
  }
}
