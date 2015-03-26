package net.magik6k.mpt.panel;

import net.magik6k.jwwf.core.Widget;
import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.enums.Type;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.jwwf.widgets.basic.panel.Row;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.view.RepoBrowser;
import net.magik6k.mpt.widget.UserProfile;

public class UserPanel extends Row {

	private MptClient user;
	
	public UserPanel(final MptClient user) {
		super(2);
		this.user = user;
		TextLabel titleLabel = new TextLabel("Logged in as <b>" + 
				user.getAuth().getUsername() + "</b>&nbsp;").setTextWrapping(false);
		
		Button logout = new Button("Logout", new ClickHandler() {
			
			@Override
			public void clicked() {
				user.logout();
			}
		}).setType(Type.WARNING);
		Button profile = new Button("My Repositories", new ClickHandler() {
			@Override
			public void clicked() {
				put(new UserProfile(user), 1);
			}
		});
		Button repoBrowser = new Button("Repository Browser", new ClickHandler() {
			@Override
			public void clicked() {
				user.userPanel.put(new RepoBrowser(user));
			}
		});
		Panel topBar = new Panel(4, titleLabel, logout, profile, repoBrowser).setWidth(12);
		this.put(topBar, 0);
		this.put(new UserProfile(user), 1);
	}
	
	public int put(Widget widget){
		this.put(widget, 1);
		return 0;
	}
	
	public Button createBackButton(String label){
		final UserPanel inst = this;
		return new Button(label, new ClickHandler() {
			
			@Override
			public void clicked() {
				inst.put(new UserProfile(user), 1);
			}
		}).setType(Type.PRIMARY);
	}
	
}
