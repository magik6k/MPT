package net.magik6k.mpt.widget;

import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.handlers.FileCreationHanlder;
import net.magik6k.mpt.handlers.YesNoHandler;
import net.magik6k.mpt.util.Holder;
import net.magik6k.mpt.util.MptFile;
import net.magik6k.mpt.view.FileEditor;

public class PackageFiles extends VerticalPanel{
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
		this.put(new TextLabel("<b>Files:</b>"));
		displayFiles(repo, pack);
	}
	
	private void displayFiles(final String repo, final String pack){
		List<MptFile> list = PackageBase.instance.getFiles(pack);
		final TablePanel grid = new TablePanel(3, list.size());
		
		for(final MptFile file : list){
			grid.put(new TextLabel(new StringBuilder("<b style='font-weight:400;'>").append(file.name).append("</b>").toString()));
			final int delid = grid.put(new InternalLink("Edit", new ClickHandler() {
				
				@Override
				public void clicked() {
					user.userPanel.put(new FileEditor(user, repo, pack, file.name));
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
		}
		
		
		this.put(grid, 2);
	}
	
}
