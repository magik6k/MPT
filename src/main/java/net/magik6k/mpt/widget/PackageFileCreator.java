package net.magik6k.mpt.widget;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.handlers.FileCreationHanlder;

public class PackageFileCreator extends Panel {

	public PackageFileCreator(final String repo, final String pack, final FileCreationHanlder handler) {
		super(12, 1);
		Row row = new Row(3);

		row.put(new Panel(3, 1, new TextLabel("Create file ").setTextWrapping(false)));
		final TextInput filename = new TextInput("Filename", new TextHandler() {
			@Override
			public void onType(String arg0) {}
		});
		row.put(new Panel(6, 1, filename));
		row.put(new Panel(1, 1, new Button("Create", new ClickHandler() {

			@Override
			public void clicked() {
				if (!PackageBase.instance.fileExists(pack, filename.getText())) {
					PackageBase.instance.createFile(filename.getText(), pack);
					handler.onCreateFile();
				}
			}
		}).setType(Type.SUCCESS)));

		this.put(row);
	}
	
}
