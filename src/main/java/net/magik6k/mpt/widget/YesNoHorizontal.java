package net.magik6k.mpt.widget;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.mpt.handlers.YesNoHandler;

public class YesNoHorizontal extends HorizontalPanel{

	public YesNoHorizontal(String msg, final YesNoHandler handler) {
		super(3, 6);
		this.put(new TextLabel(msg));
		this.put(new InternalLink("Yes", new ClickHandler() {
			
			@Override
			public void clicked() {
				handler.yes();
			}
		}));

		this.put(new InternalLink("No", new ClickHandler() {
	
			@Override
			public void clicked() {
				handler.no();
			}
		}));
	}
	
}
