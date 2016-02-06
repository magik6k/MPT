package net.magik6k.mpt.client.util

import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.{img, span}

object smallicon {
  def apply(i: String): Tag = {
    span(img().src(s"/assets/icons/$i.svg")).withClass("icon", "smallicon")
  }
}
