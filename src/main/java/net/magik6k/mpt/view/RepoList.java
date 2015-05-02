package net.magik6k.mpt.view;

import java.util.Collections;
import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.RepoBase;

public class RepoList extends Row {

	private RepoList(final MptClient user, int repoCount) {
		super(2);
		this.put(new Panel(new TextLabel("You have access to <b>" + String.valueOf(repoCount) + "</b> repos")
				.setTextWrapping(false)).setWidth(12));
		
		TablePanel repoList = new TablePanel(3, repoCount);
		List<String> repos = RepoBase.instance.getUserRepos(user.getAuth().getUsername());
		Collections.sort(repos);

		for(final String repo : repos){
			repoList.put(new TextLabel("<b style='font-weight:400;'>"+repo+"</b>"));
			repoList.put(new InternalLink("View", new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new Repo(user, repo));
				}
			}));
			repoList.put(new InternalLink("Settings", new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new RepoSettings(user, repo));
				}
			}));
		}
		
		this.put(new Panel(repoList).setWidth(12));
	}
	
	public RepoList(MptClient user) {
		this(user, RepoBase.instance.getUserRepoCount(user.getAuth().getUsername()));
	}
	
}
