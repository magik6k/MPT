package net.magik6k.mpt.widget;

import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.view.Pack;

public class RepoPackages extends VerticalPanel{

	public RepoPackages(final MptClient user, final String repo) {
		super(2);
		this.put(new TextLabel("<b>Packages:</b>"));
		List<String> packs = PackageBase.instance.getPackagesForRepo(repo);
		
		TablePanel packageList = new TablePanel(2, packs.size());
		for(final String pack : packs){
			packageList.put(new InternalLink(pack, new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new Pack(user, repo, pack));
				}
			}));
			packageList.put(new TextLabel("Delete"));
		}
		
		this.put(packageList);
	}
}
