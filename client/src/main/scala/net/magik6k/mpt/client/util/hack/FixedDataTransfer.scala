package net.magik6k.mpt.client.util.hack

import org.scalajs.dom.Element
import org.scalajs.dom.raw.DragEvent
import scala.scalajs.js
@js.native
trait FixedDataTransfer extends DragEvent {
  def setDragImage(img: Element, xOffset: Double, yOffset: Double): js.Any = js.native
}
