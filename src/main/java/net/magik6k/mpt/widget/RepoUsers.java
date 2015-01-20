package net.magik6k.mpt.widget;

import java.util.List;

import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.InternalLink;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.TablePanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.db.RepoBase;

public class RepoUsers extends VerticalPanel{

	private final String repo;
	private final MptClient user;
	
	public RepoUsers(final MptClient user, final String repo) {
		super(2);
		
		this.repo = repo;
		this.user = user;
		
		this.put(new TextLabel("<h3>Users with access:</h3>"));
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
		
		final TextInput uname = new TextInput("Username");
		Button add = new Button("Add collaborator", new ClickHandler() {
			
			@Override
			public void clicked() {
				if(!uname.getText().isEmpty()&&!RepoBase.instance.hasUser(uname.getText(), repo)){
					RepoBase.instance.addUser(repo, uname.getText());
					redraw();
				}
			}
		});
		
		if(owner){
			list.put(uname);
			list.put(add);
		}else{
			this.put(new HorizontalPanel(2, uname, add));
		}
		
		this.put(list, 1);
	}
	
}
