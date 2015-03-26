package net.magik6k.mpt.widget;

import java.util.List;

import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.handlers.TextHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.RepoBase;
import net.magik6k.mpt.db.UserBase;

public class RepoUsers extends Panel {

	private final String repo;
	private final MptClient user;
	
	public RepoUsers(final MptClient user, final String repo) {
		super(12, 2);
		
		this.repo = repo;
		this.user = user;
		
		this.put(new TextLabel("<h3>Users with access to this repository:</h3>"));
		redraw();
	
	}

	private void redraw(){
		boolean owner = RepoBase.instance.isOwner(user.getAuth().username, repo);
		List<String> users = RepoBase.instance.getRepoUsers(repo);
		
		TablePanel list = new TablePanel(owner ? 2 : 1, users.size()+(owner ? 2 : 1));
		for(String usr : users){
			final String u = usr;
			list.put(new TextLabel(usr));
			if(owner && !usr.equals(user.getAuth().username)){
				list.put(new InternalLink("Remove",new ClickHandler() {
					
					@Override
					public void clicked() {
						RepoBase.instance.removeUser(repo, u);
						redraw();
					}
				}));
			}else if(owner){
				list.put(new TextLabel("-unremovable-"));
			}
		}
		
		final TextInput username = new TextInput("Username");
		username.setTextHandler(new TextHandler() {
			@Override
			public void onType(String name) {
				if(!name.isEmpty()&&!RepoBase.instance.hasUser(name, repo)&&UserBase.instance.userExists(name)){
					username.setStatus(Type.SUCCESS);
				} else if (!name.isEmpty()) {
					username.setStatus(Type.DANGER);
				} else {
					username.setStatus(Type.DEFAULT);
				}
			}
		});
		Button add = new Button("Add collaborator", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(!username.getText().isEmpty()&&!RepoBase.instance.hasUser(username.getText(), repo)){
					RepoBase.instance.addUser(repo, username.getText());
					redraw();
				}
			}
		}).setType(Type.DANGER);
		
		if(owner){
			list.put(username);
			list.put(add);
		}else{
			this.put(new Panel(2, username, add));
		}
		
		this.put(list, 1);
	}
	
}
