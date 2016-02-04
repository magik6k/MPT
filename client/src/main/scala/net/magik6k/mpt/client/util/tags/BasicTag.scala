package net.magik6k.mpt.client.util.tags

import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLElement

abstract class BasicTag(tagName: String, inner: Tag*) extends Tag(inner: _*) {
  val element = document.createElement(tagName)
  inner.foreach(tag => element.appendChild(tag.getNode))

  override def getNode: Node = element
  def withClass(clazz: String) = {
    element.classList.add(clazz)
    this
  }

  def css(toSet: (String, String)*) = {
    toSet.foreach{
      case (name: String, value: String) =>
        element.asInstanceOf[HTMLElement].style.setProperty(name, value)
    }
    this
  }

  def setVisible(visible: Boolean) = visible match {
    case true => element.asInstanceOf[HTMLElement].style.setProperty("visibility", "visible")
    case false => element.asInstanceOf[HTMLElement].style.setProperty("visibility", "hidden")
  }
}
