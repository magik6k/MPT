package net.magik6k.mpt.client.util


import org.scalajs.dom.Event
import org.scalajs.dom.raw.XMLHttpRequest

import scala.scalajs.js
import scala.scalajs.js.JSON

object REST {
  def get[A >: Null](url: String, done: (js.Dynamic) => Any): Unit = {
    println(s"GET $url")

    val req = new XMLHttpRequest
    req.open("GET", url, async = true)

    req.onload = { (e: Event) =>
      println("GET OK")
      done(JSON.parse(req.responseText))
    }

    req.onerror = { (e: Event) =>
      println(s"GET ERROR: $url")
      done(null)
    }

    req.send()
  }

  def post[A](url: String, data: js.Dynamic, done: (Boolean) => Any): Unit = {
    println(s"POST $url")

    val req = new XMLHttpRequest
    req.open("POST", url, async = true)
    req.setRequestHeader("Content-Type", "text/json")

    req.onload = { (e: Event) =>
      done(true)
    }

    req.onerror = { (e: Event) =>
      println(s"POST ERROR: $url")
      done(false)
    }

    req.send(JSON.stringify(data))
  }
}
