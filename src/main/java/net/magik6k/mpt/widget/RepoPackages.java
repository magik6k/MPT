package net.magik6k.mpt.widget;

import java.util.Collections;
import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.handlers.YesNoHandler;
import net.magik6k.mpt.view.Pack;

public class RepoPackages extends Panel{
	
	private final MptClient user;
	private final String repo;
	
	public RepoPackages(final MptClient user, final String repo) {
		super(12, 2);
		this.user = user;
		this.repo = repo;
		
		this.put(new TextLabel("<b>Packages:</b>"));
		
		makeList();
	}
	
	private void makeList(){
		List<String> packs = PackageBase.instance.getPackagesForRepo(repo);
		Collections.sort(packs);

		final TablePanel packageList = new TablePanel(2, packs.size());
		int row = 0;
		for(final String pack : packs){
			packageList.put(new InternalLink(pack, new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new Panel(12, 1, new Pack(user, repo, pack)));
				}
			}));
			final int r = row++;
			packageList.put(new InternalLink("Delete", new ClickHandler() {
				
				@Override
				public void clicked() {
					packageList.put(new YesNoHorizontal("REALLY DELETE?", new YesNoHandler() {
						
						@Override
						public void yes() {
							PackageBase.instance.removePackage(pack);
							makeList();
						}
						
						@Override
						public void no() {
							makeList();
						}
					}), r, 1);
					
					
				}
			}));
		}
		
		this.put(packageList, 1);
	}
}
