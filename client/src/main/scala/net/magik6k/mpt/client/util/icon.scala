package net.magik6k.mpt.client.util

import net.magik6k.mpt.client.util.tags.Tag
import net.magik6k.mpt.client.util.tags.Tags.{span, img}

object icon {
  def apply(i: String): Tag = {
    span(img().src(s"/assets/icons/$i.svg")).withClass("icon")
  }
}
