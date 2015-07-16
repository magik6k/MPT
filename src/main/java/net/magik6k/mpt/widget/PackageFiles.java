package net.magik6k.mpt.widget;

import java.util.Collections;
import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.ExternalLink;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.handlers.FileCreationHanlder;
import net.magik6k.mpt.handlers.YesNoHandler;
import net.magik6k.mpt.util.Holder;
import net.magik6k.mpt.util.MptFile;
import net.magik6k.mpt.view.FileEditor;

public class PackageFiles extends Row {
	private final MptClient user;
	
	public PackageFiles(final MptClient user, final String repo, final String pack) {
		super(3);
		this.user = user;
		this.put(new PackageFileCreator(repo, pack, new FileCreationHanlder() {
			
			@Override
			public void onCreateFile() {
				displayFiles(repo, pack);
			}
		}));
		this.put(new Panel(12, 1, new TextLabel("<b>Files:</b>")));
		displayFiles(repo, pack);
	}
	
	private void displayFiles(final String repo, final String pack){
		List<MptFile> list = PackageBase.instance.getFiles(pack);
		Collections.sort(list);
		final TablePanel grid = new TablePanel(4, list.size());
		
		for(final MptFile file : list){
			grid.put(new TextLabel(new StringBuilder("<b style='font-weight:400;'>").append(file.name).append("</b>").toString()));
			final int delid = grid.put(new InternalLink("Edit", new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new FileEditor(user, repo, pack, file.name).asPanel(12));
					user.setTitle(file.name + " - " + pack + " - " + repo);
				}
			}));
			
			final Holder<InternalLink> linkHolder = new Holder<>();
			
			final InternalLink delete = new InternalLink("Delete", new ClickHandler() {
				
				@Override
				public void clicked() {
					grid.put(new YesNoHorizontal("Really delete? ", new YesNoHandler() {
						
						@Override
						public void yes() {
							PackageBase.instance.removeFile(file.name, pack);
							displayFiles(repo, pack);
						}
						
						@Override
						public void no() {
							grid.put(linkHolder.object, delid, 2);
						}
					}), delid, 2);
				}
			});
			linkHolder.object = delete;
			grid.put(delete);
			grid.put(new ExternalLink("/api/file/" + pack + file.name, "Direct link"));
		}
		
		
		this.put(new Panel(12, 1, grid), 2);
	}
	
}
