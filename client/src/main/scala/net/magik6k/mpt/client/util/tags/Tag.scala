package net.magik6k.mpt.client.util.tags

import org.scalajs.dom._
import org.scalajs.jquery._

import scala.scalajs.js


abstract class Tag(inner: Tag*) {
  def getNode: Node

  def jq: JQuery = jQuery(getNode)

  def find(selector: String): JQuery = jq.find(selector)
  def find(element: js.Any): JQuery = jq.find(element)
  def find(obj: JQuery): JQuery = jq.find(obj)
}

object Tag {
  implicit def ofString(str: String): Tag = {
    new Tag() {
      val element = document.createElement("span")
      element.innerHTML = str.replaceAll(">","&gt;").replaceAll("<","&lt;")
      override def getNode: Node = element
    }
  }

  implicit def ofElement(element: Element): Tag = {
    new Tag() {
      override def getNode: Node = element

    }
  }

  implicit def elementTag(tag: Tag): Element = tag.getNode.asInstanceOf[Element]

  def apply(element: Element) = ofElement(element)
}

