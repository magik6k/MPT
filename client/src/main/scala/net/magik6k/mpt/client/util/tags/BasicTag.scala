package net.magik6k.mpt.client.util.tags

import org.scalajs.dom._
import org.scalajs.dom.raw.{HTMLImageElement, HTMLElement}

abstract class BasicTag(tagName: String, inner: Tag*) extends Tag(inner: _*) {
  val element = document.createElement(tagName)
  inner.foreach(tag => element.appendChild(tag.getNode))

  override def getNode: Node = element
  def withClass(clazz: String*) = {
    clazz.foreach(element.classList.add)
    this
  }

  def hasClass(clazz: String) = {
    element.classList.contains(clazz)
  }

  def removeClass(clazz: String) = {
    element.classList.remove(clazz)
  }

  def toggleClass(clazz: String) = {
    if(hasClass(clazz))
      removeClass(clazz)
    else
      withClass(clazz)
  }

  def set(inner: Tag*): Unit = {
    while(element.firstChild != null) element.removeChild(element.firstChild)
    inner.foreach(tag => element.appendChild(tag.getNode))
  }

  def css(toSet: (String, String)*) = {
    toSet.foreach{
      case (name: String, value: String) =>
        element.asInstanceOf[HTMLElement].style.setProperty(name, value)
    }
    this
  }

  def onblur(cb: (FocusEvent)=>Unit) = {element.asInstanceOf[HTMLElement].onblur = cb; this}
  def onfocus(cb: (FocusEvent)=>Unit) = {element.asInstanceOf[HTMLElement].onfocus = cb; this}

  def onmouseleave(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onmouseleave = cb; this}
  def onmouseenter(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onmouseenter = cb; this}
  def onmouseup(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onmouseup = cb; this}
  def onmouseover(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onmouseover = cb; this}
  def onmousedown(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onmousedown = cb; this}
  def onclick(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onclick = cb; this}
  def onmousemove(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onmousemove = cb; this}
  def ondblclick(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].ondblclick = cb; this}
  def oncontextmenu(cb: (MouseEvent=>Unit)) = {element.asInstanceOf[HTMLElement].oncontextmenu = cb; this}

  def onkeydown(cb: (KeyboardEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onkeydown = cb; this}
  def onkeyup(cb: (KeyboardEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onkeyup = cb; this}
  def onkeypress(cb: (KeyboardEvent=>Unit)) = {element.asInstanceOf[HTMLElement].onkeypress = cb; this}

  def onchange(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onchange = cb; this}
  def onreset(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onreset = cb; this}
  def onhelp(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onhelp = cb; this}
  def onseeked(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onseeked = cb; this}
  def onemptied(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onemptied = cb; this}
  def onseeking(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onseeking = cb; this}
  def oncanplay(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].oncanplay = cb; this}
  def ondeactivate(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].ondeactivate = cb; this}
  def onloadstart(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onloadstart = cb; this}
  def ondragenter(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].ondragenter = cb; this}
  def onsubmit(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onsubmit = cb; this}
  def oncanplaythrough(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].oncanplaythrough = cb; this}
  def onsuspend(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onsuspend = cb; this}
  def onpause(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onpause = cb; this}
  def onwaiting(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onwaiting = cb; this}
  def onratechange(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onratechange = cb; this}
  def onloadedmetadata(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onloadedmetadata = cb; this}
  def onplay(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onplay = cb; this}
  def onplaying(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onplaying = cb; this}
  def onreadystatechange(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onreadystatechange = cb; this}
  def onloadeddata(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onloadeddata = cb; this}
  def onended(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].onended = cb; this}
  def oninput(cb: (Event=>Unit)) = {element.asInstanceOf[HTMLElement].oninput = cb; this}

  def src(s: String) = {element.asInstanceOf[HTMLImageElement].src = s; this}

  def setVisible(visible: Boolean) = visible match {
    case true => show()
    case false => hide()
  }
}
