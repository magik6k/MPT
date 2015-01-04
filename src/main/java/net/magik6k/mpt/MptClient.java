package net.magik6k.mpt;

import java.io.PrintWriter;
import java.io.StringWriter;

import net.magik6k.jwwf.core.MainFrame;
import net.magik6k.jwwf.core.User;
import net.magik6k.jwwf.widgets.basic.PreformattedTextLabel;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.action.LoginHandler;
import net.magik6k.mpt.panel.LoginPanel;
import net.magik6k.mpt.panel.UserPanel;

public class MptClient extends User{
	
	private final UserAuth auth = new UserAuth();
	public UserPanel userPanel;
	
	@Override
	protected void initializeUser(final MainFrame rootFrame) {
		viewLoginPanel();		
	}
	
	public void logout(){
		auth.logout();
		userPanel = null;
		viewLoginPanel();
	}
	
	public UserAuth getAuth(){
		return auth;
	}
	
	public void handleCriticalError(Exception ex){
		TextLabel fail = new TextLabel("User has performed illegal action:");
		PreformattedTextLabel exceptionLabel = new PreformattedTextLabel(ex.getMessage());
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		PreformattedTextLabel stackLabel = new PreformattedTextLabel(sw.toString());
		rootFrame.put(new VerticalPanel(3, fail, exceptionLabel, stackLabel));
	}

	private void viewLoginPanel(){
		final MptClient clinetInstance = this;
		rootFrame.put(new LoginPanel(new LoginHandler() {
			
			@Override
			public void login(String user, String password) {
				System.out.printf("login %s\n", user);
				auth.loginAs(user);
				userPanel = new UserPanel(clinetInstance);
				rootFrame.put(userPanel);
			}
		}, this));
	}
}
