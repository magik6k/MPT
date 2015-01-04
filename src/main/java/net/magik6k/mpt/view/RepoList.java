package net.magik6k.mpt.view;

import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.RepoBase;

public class RepoList extends VerticalPanel{

	private RepoList(final MptClient user, int repoCount) {
		super(2);
		this.put(new TextLabel("You have access to <b>" + String.valueOf(repoCount) + "</b> repos")
				.setTextWrapping(false));
		
		TablePanel repoList = new TablePanel(3, repoCount);
		List<String> repos = RepoBase.instance.getUserRepos(user.getAuth().getUsername());
		
		for(final String repo : repos){
			repoList.put(new TextLabel("<b>"+repo+"</b>"));
			repoList.put(new InternalLink("View", new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new Repo(user, repo));
				}
			}));
			repoList.put(new TextLabel("Settings"));
		}
		
		this.put(repoList);
	}
	
	public RepoList(MptClient user) {
		this(user, RepoBase.instance.getUserRepoCount(user.getAuth().getUsername()));		
	}
	
}
