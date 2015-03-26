package net.magik6k.mpt.view;

import net.magik6k.jwwf.core.Widget;
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
import net.magik6k.mpt.db.RepoBase;
import net.magik6k.mpt.widget.UserProfile;

public class RepoCreator extends Row {

	public RepoCreator(final MptClient client) {
		super(4);
		this.put(new TextLabel("<h3>Creating new repository</h3>").setTextWrapping(false).asPanel(12));
		
		final TextInput repoNameInput = new TextInput("Name");
		repoNameInput.setTextHandler(new TextHandler() {
			@Override
			public void onType(String text) {
				if(repoNameInput.getText().length() < 1) {
					repoNameInput.setStatus(Type.DEFAULT);
				} else if(repoNameInput.getText().length() < 3) {
					repoNameInput.setStatus(Type.WARNING);
				} else if(RepoBase.instance.exists(repoNameInput.getText())){
					repoNameInput.setStatus(Type.DANGER);
				} else {
					repoNameInput.setStatus(Type.SUCCESS);
				}
			}
		});

		this.put(new TextLabel("Name: ").setTextWrapping(false).asPanel(2));
		this.put(repoNameInput.asPanel(10));
		
		final RepoCreator inst = this;
		this.put(new Button("Create!", Type.SUCCESS, new ClickHandler() {
			
			@Override
			public void clicked() {
				if(repoNameInput.getText().length() < 3){
					inst.put(new TextLabel("Repo name must be longer than 3 characters").asPanel(9), 1);
					inst.put(client.userPanel.createBackButton("Back to profile").asPanel(3) ,2);
					inst.put((Widget)null ,3);
				}else if(RepoBase.instance.exists(repoNameInput.getText())){
					inst.put(new TextLabel("Repo with this name already exists").asPanel(9), 1);
					inst.put(client.userPanel.createBackButton("Back to profile").asPanel(3), 2);
					inst.put((Widget)null ,3);
				} else {
					RepoBase.instance.addRepo(repoNameInput.getText(), client.getAuth().getUsername());
					client.userPanel.put(new UserProfile(client), 1);
				}
			}
		}).asPanel(2));
	}
	
}
