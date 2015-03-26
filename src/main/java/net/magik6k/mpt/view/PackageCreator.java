package net.magik6k.mpt.view;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;

public class PackageCreator extends Row {

	public PackageCreator(final MptClient client, final String repoName) {
		super(4);
		this.put(new TextLabel("<h3>Creating new package in <b>"+repoName+"</b></h3>").setTextWrapping(false).asPanel(12));
		
		final TextInput packageNameInput = new TextInput("Name");
		packageNameInput.setTextHandler(new TextHandler() {
			@Override
			public void onType(String text) {
				if(packageNameInput.getText().length() < 1){
					packageNameInput.setStatus(Type.DEFAULT);
				}else if(PackageBase.instance.exists(packageNameInput.getText())){
					packageNameInput.setStatus(Type.DANGER);
				}else{
					packageNameInput.setStatus(Type.SUCCESS);
				}
			}
		});

		this.put(new TextLabel("Name: ").setTextWrapping(false).asPanel(2));
		this.put(packageNameInput.asPanel(10));
		
		final PackageCreator inst = this;
		
		this.put(new Row(2, new Button("Create!", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(packageNameInput.getText().length() < 1){
					inst.put(new TextLabel("Package name must be longer than 1 character").setTextWrapping(false), 1);
				}else if(PackageBase.instance.exists(packageNameInput.getText())){
					inst.put(new TextLabel("Package with this name already exists").setTextWrapping(false), 1);
				}else{
					PackageBase.instance.addPackage(packageNameInput.getText(), repoName);
					inst.put(new TextLabel("Package created!").setTextWrapping(false), 1);
				}
				
				inst.put(new Button("Back to repo", new ClickHandler() {
					
					@Override
					public void clicked() {
						client.userPanel.put(new Repo(client, repoName));
					}
				}) ,2);
			}
		}).setType(Type.SUCCESS), new Button("Back to repo", new ClickHandler() {
			
			@Override
			public void clicked() {
				client.userPanel.put(new Repo(client, repoName));
			}
		})));
	}
}
