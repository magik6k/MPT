package net.magik6k.mpt.view;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.util.NamedWidget;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.TabbedPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.widget.PackageFiles;

public class Pack extends VerticalPanel{

	public Pack(final MptClient user, final String repo, String pack) {
		super(2);
		this.put(new HorizontalPanel(2, new TextLabel("Package: <b>"+pack+"</b>"),new Button("Back to repo", new ClickHandler() {
			
			@Override
			public void clicked() {
				user.userPanel.put(new Repo(user, repo));
			}
		})));
		
		TabbedPanel cont = new TabbedPanel(2,
			new NamedWidget(new PackageFiles(user, repo, pack), "Files"),
			new NamedWidget(new TextLabel("TODO"), "Stats"));
		this.put(cont);
		this.setElementSpacing(16);
	}

}
