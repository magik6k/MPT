package net.magik6k.mpt.widget;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.handlers.FileCreationHanlder;

public class PackageFileCreator extends HorizontalPanel{

	public PackageFileCreator(final String repo, final String pack, final FileCreationHanlder handler) {
		super(3, 8);
		this.put(new TextLabel("Create file ").setTextWrapping(false));
		final TextInput filename = new TextInput("Filename", new TextHandler() {
			@Override
			public void onType(String arg0) {}
		});
		this.put(filename);
		this.put(new Button("Create", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(!PackageBase.instance.fileExists(pack, filename.getText())){
					PackageBase.instance.createFile(filename.getText(), pack);
					handler.onCreateFile();
				}				
			}
		}));
		this.setElementAlign(PanelAlign.MIDDLE);		
	}
	
}
