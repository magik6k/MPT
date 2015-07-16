package net.magik6k.mpt.view;

import net.magik6k.jwwf.ace.AceEditor;
import net.magik6k.jwwf.ace.AceMode;
import net.magik6k.jwwf.ace.AceTheme;
import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.CheckHandler;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.ExternalLink;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.CheckButton;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.plugin.FadeOutLabel;

public class FileEditor extends Row {

	public FileEditor(final MptClient user, final String repo, final String pack, final String file) {
		super(3);

		Panel menu = new Panel(12, 3);
		menu.put(new Button("Back to Package", Type.PRIMARY, new ClickHandler() {

			@Override
			public void clicked() {
				user.userPanel.put(new Pack(user, repo, pack).asPanel(12));
				user.setTitle(pack + " - " + pack);
			}
		}));
		menu.put(new TextLabel("Editting "+pack+":<b>"+file+"</b> "));
		menu.put(new ExternalLink("/api/file/" + pack + file, "Direct link"));

		this.put(menu);
		
		final FadeOutLabel actionLabel = new FadeOutLabel("Editting "+pack+":<b>"+file+"</b>");
		class AutoSave {public boolean enable = false;public int counter = 0;public AceEditor editor;}
		final AutoSave autoSave = new AutoSave();
		
		final AceEditor editor = new AceEditor(PackageBase.instance.getFile(pack, file).content, new TextHandler() {
			
			@Override
			public void onType(String text) {
				if(autoSave.enable/*&& autoSave.counter++%20==0*/){
					PackageBase.instance.updateFile(pack, file, autoSave.editor.getText());
					actionLabel.setText("Auto-Saved");
				}
			}
		}, 800,  AceMode.LUA, AceTheme.VIBRANT_INK);
		
		autoSave.editor = editor;
		
		Button save = new Button("Save", Type.SUCCESS, new ClickHandler() {
			
			@Override
			public void clicked() {
				PackageBase.instance.updateFile(pack, file, editor.getText());
				actionLabel.setText("Saved");
			}
		});
		
		CheckButton autoSaveButton = new CheckButton("Autosave", new CheckHandler() {
			@Override
			public void checked(boolean state) {
				autoSave.enable = state;
			}
		});
		
		this.put(new Panel(12, 3, save, autoSaveButton, actionLabel));
		this.put(new Panel(12, 1, editor));
	}

}
