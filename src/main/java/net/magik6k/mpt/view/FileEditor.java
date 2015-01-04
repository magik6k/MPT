package net.magik6k.mpt.view;

import net.magik6k.jwwf.ace.AceEditor;
import net.magik6k.jwwf.ace.AceMode;
import net.magik6k.jwwf.ace.AceTheme;
import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.CheckHandler;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.CheckButton;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.plugin.FadeOutLabel;

public class FileEditor extends VerticalPanel{

	public FileEditor(final MptClient user, final String repo, final String pack, String file) {
		super(3);
		HorizontalPanel menu = new HorizontalPanel(2, 32).setElementAlign(PanelAlign.MIDDLE);
		
		menu.put(new TextLabel("Editting "+pack+":<b>"+file+"</b>"));
		menu.put(new Button("Back to Package", new ClickHandler() {
			
			@Override
			public void clicked() {
				user.userPanel.put(new Pack(user, repo, pack));
			}
		}));
		
		this.put(menu);
		
		final FadeOutLabel actionLabel = new FadeOutLabel("Editting "+pack+":<b>"+file+"</b>");
		class Autosave {public boolean enable = false;public int counter = 0;public AceEditor editor;}
		final Autosave autosave = new Autosave();
		
		final AceEditor editor = new AceEditor(PackageBase.instance.getFile(pack, file).content, new TextHandler() {
			
			@Override
			public void onType(String text) {
				if(autosave.enable&&autosave.counter++%20==0){
					PackageBase.instance.updateFile(pack, file, autosave.editor.getText());
					actionLabel.setText("Auto-Saved");
				}
			}
		}, AceMode.LUA, AceTheme.VIBRANT_INK);
		
		autosave.editor = editor;
		
		Button save = new Button("Save", new ClickHandler() {
			
			@Override
			public void clicked() {
				PackageBase.instance.updateFile(pack, file, editor.getText());
				actionLabel.setText("Saved");
			}
		});
		
		CheckButton autosaveButton = new CheckButton("Autosave", new CheckHandler() {
			@Override
			public void checked(boolean state) {
				autosave.enable = state;
			}
		});
		
		this.put(new HorizontalPanel(3, save, autosaveButton, actionLabel).setElementAlign(PanelAlign.MIDDLE));		
		this.put(editor);
	}

}
