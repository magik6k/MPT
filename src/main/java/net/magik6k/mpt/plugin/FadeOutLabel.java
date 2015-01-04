package net.magik6k.mpt.plugin;

import net.magik6k.jwwf.widgets.basic.TextLabel;

public class FadeOutLabel extends TextLabel{

	public FadeOutLabel(String text) {
		super(text);
	}

	@Override
	public String getName() {
		return "FadeOutLabel";
	}
}
