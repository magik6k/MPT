package net.magik6k.mpt.view;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.PackageBase;

public class PackageCreator extends VerticalPanel{

	public PackageCreator(final MptClient client, final String repoName) {
		super(3);
		this.put(new TextLabel("<h3>Creating new package in <b>"+repoName+"</b></h3>").setTextWrapping(false));
		
		final TextInput packageNameInput = new TextInput("Name");
		
		this.put(new HorizontalPanel(2, new TextLabel("Name of new package: ").setTextWrapping(false), packageNameInput)
			.setElementAlign(PanelAlign.MIDDLE));
		
		final PackageCreator inst = this;
		
		this.put(new HorizontalPanel(2, new Button("Create!", new ClickHandler() {
			
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
		}), new Button("Back to repo", new ClickHandler() {
			
			@Override
			public void clicked() {
				client.userPanel.put(new Repo(client, repoName));
			}
		})));
	}
	
}
