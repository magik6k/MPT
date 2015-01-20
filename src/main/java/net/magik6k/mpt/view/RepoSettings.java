package net.magik6k.mpt.view;

import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.widget.RepoUsers;

public class RepoSettings extends VerticalPanel{

	public RepoSettings(final MptClient user, String pack) {
		super(3);
		this.put(user.userPanel.createBackButton("Back to user"));
		this.put(new TextLabel("<h2>Repository settings: "+pack+"</h2>"));
		this.put(new RepoUsers(user, pack));
	}

}
