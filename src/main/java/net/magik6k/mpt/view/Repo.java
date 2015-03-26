package net.magik6k.mpt.view;

import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.widget.RepoPackages;

public class Repo extends Row {

	public Repo(final MptClient user, final String repoName) {
		super(3);
		
		this.put(new TextLabel("<h4>Repository: <b>"+repoName+"</b></h4>").asPanel(12));
		
		Panel repoManager = new Panel(12, 2);
		repoManager.put(new Button("Create package", Type.SUCCESS, new ClickHandler() {
			
			@Override
			public void clicked() {
				user.userPanel.put(new PackageCreator(user, repoName));
			}
		}));
		
		repoManager.put(user.userPanel.createBackButton("Back to user"));
		
		this.put(repoManager);
		this.put(new RepoPackages(user, repoName));
		
	}
	
}
