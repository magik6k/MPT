package net.magik6k.mpt.client.util

import java.nio.ByteBuffer

import boopickle.Default._
import boopickle.Pickler
import org.scalajs.dom.Event
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.jquery._

import scala.scalajs.js
import scala.scalajs.js.typedarray.{Uint8Array, ArrayBuffer, Int8Array}

object REST {
  def get[A >: Null](url: String, done: (A) => Any)(implicit u: Pickler[A]): Unit = {
    println(s"GET $url")

    val req = new XMLHttpRequest
    req.open("GET", url, async = true)
    req.responseType = "arraybuffer"

    req.onload = { (e: Event) =>
      println("GET OK")
      done(Unpickle[A].fromBytes(
        ByteBuffer.wrap(new Int8Array(req.response.asInstanceOf[ArrayBuffer]).toArray)))
    }

    req.onerror = { (e: Event) =>
      println(s"GET ERROR: $url")
      done(null)
    }

    req.send()
  }

  def post[A](url: String, data: A, done: (Boolean) => Any)(implicit u: Pickler[A]): Unit = {
    println(s"POST $url")
    import js.JSConverters._
    val buf: ByteBuffer = Pickle.intoBytes(data)
    val array = new Array[Byte](buf.remaining)
    buf.get(array, 0, array.length)

    val req = new XMLHttpRequest
    req.open("POST", url, async = true)
    req.responseType = "arraybuffer"

    req.onload = { (e: Event) =>
      done(true)
    }

    req.onerror = { (e: Event) =>
      println(s"POST ERROR: $url")
      done(false)
    }
    req.send(new Int8Array(array.toJSArray))
  }
}
