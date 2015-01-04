package net.magik6k.mpt.view;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.RepoBase;

public class RepoCreator extends VerticalPanel{

	public RepoCreator(final MptClient client) {
		super(3);
		this.put(new TextLabel("<h3>Creating new repository</h3>").setTextWrapping(false));
		
		final TextInput repoNameInput = new TextInput("Name");
		
		this.put(new HorizontalPanel(2, new TextLabel("Name of new repository: ").setTextWrapping(false), repoNameInput)
			.setElementAlign(PanelAlign.MIDDLE));
		
		final RepoCreator inst = this;
		this.put(new Button("Create!", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(repoNameInput.getText().length() < 3){
					inst.put(new TextLabel("Repo name must be longer than 3 characters").setTextWrapping(false), 1);
				}else if(RepoBase.instance.exists(repoNameInput.getText())){
					inst.put(new TextLabel("Repo with this name already exists").setTextWrapping(false), 1);
				}else{
					RepoBase.instance.addRepo(repoNameInput.getText(), client.getAuth().getUsername());
					inst.put(new TextLabel("Repo created!").setTextWrapping(false), 1);
				}
				inst.put(client.userPanel.createBackButton("Back to profile") ,2);
			}
		}));
	}
	
}
