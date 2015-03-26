package net.magik6k.mpt.widget;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.handlers.RepoCreationHandler;
import net.magik6k.mpt.view.RepoList;

public class UserProfile extends Panel {

	public UserProfile(MptClient client) {
		super(2);
		this.setWidth(12);
		this.put(new Row(2
				, new TextLabel("<h2 style='margin-bottom:0'>Repositories </h2>").asPanel()
				, new Button("Create repo", new RepoCreationHandler(client)).setType(Type.SUCCESS).asPanel()));
		this.put(new RepoList(client));
	}
}
