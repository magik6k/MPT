package net.magik6k.mpt.view;

import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.util.Tab;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.jwwf.widgets.basic.panel.TabbedPanel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.widget.PackDependencies;
import net.magik6k.mpt.widget.PackageFiles;

public class Pack extends Row {

	public Pack(final MptClient user, final String repo, String pack) {
		super(2);
		this.put(new Panel(12, 2, new Button("Back to repo", new ClickHandler() {
			
			@Override
			public void clicked() {
				user.userPanel.put(new Repo(user, repo));
				user.setTitle(repo + " - Repo");
			}
		}).setType(Type.PRIMARY), new TextLabel("Package: <b style='font-size:21px;'>"+pack+"</b>")));
		
		TabbedPanel cont = new TabbedPanel(3,
			new Tab(new PackageFiles(user, repo, pack), "Files", Type.PRIMARY),
			new Tab(new PackDependencies(pack), "Dependencies"),
			new Tab(new TextLabel("TODO"), "Stats"));
		this.put(new Panel(12, 1, cont));
	}
}
