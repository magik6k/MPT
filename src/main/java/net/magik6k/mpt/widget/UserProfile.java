package net.magik6k.mpt.widget;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.handlers.RepoCreationHandler;
import net.magik6k.mpt.view.RepoList;

public class UserProfile extends VerticalPanel{

	public UserProfile(MptClient client) {
		super(2);
		this.put(new HorizontalPanel(2
				, new TextLabel("<h2 style='margin-bottom:0'>Repositories </h2>")
				, new Button("Create repo", new RepoCreationHandler(client)))
			.setElementSpacing(3).setElementAlign(PanelAlign.MIDDLE));
		this.put(new RepoList(client));
	}
}
