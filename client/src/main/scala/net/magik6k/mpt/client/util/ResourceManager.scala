package net.magik6k.mpt.client.util

import org.scalajs.jquery.{JQueryXHR, JQueryAjaxSettings, jQuery}

import scala.scalajs.js

object ResourceManager {
  /**
    * Perform async AJAX GET
    * @param url URL to request
    * @param done Handler, data string is passed on success, null on error
    */
  def get(url: String, done: ((String) => Any)): Unit = {
    println(s"Get $url")

    jQuery.ajax(js.Dynamic.literal(
      url = url,
      success = { (data: js.Any, textStatus: String, jqXHR: JQueryXHR) =>
        done(data.toString)
      },
      error = { (jqXHR: JQueryXHR, textStatus: String, errorThrow: String) =>
        println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
        try {
          done(null)
        } catch {
          case e: Any => println("Error on invocation of file handler:" + e.toString)
        }
      },
      dataType = "text",
      `type` = "GET"
    ).asInstanceOf[JQueryAjaxSettings])
  }

  def sync(url: String): String = {
    println(s"SYNC! Get $url")

    var res: String = null

    jQuery.ajax(js.Dynamic.literal(
      url = url,
      async = false,
      success = { (data: js.Any, textStatus: String, jqXHR: JQueryXHR) =>
        res = data.toString
      },
      error = { (jqXHR: JQueryXHR, textStatus: String, errorThrow: String) =>
        println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
      },
      dataType = "text",
      `type` = "GET"
    ).asInstanceOf[JQueryAjaxSettings])

    res
  }
}
