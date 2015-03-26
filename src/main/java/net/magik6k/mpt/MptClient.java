package net.magik6k.mpt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.magik6k.jwwf.core.MainFrame;
import net.magik6k.jwwf.core.User;
import net.magik6k.jwwf.oauth.OAuth2Button;
import net.magik6k.jwwf.oauth.OAuthHandler;
import net.magik6k.jwwf.widgets.basic.ExternalLink;
import net.magik6k.jwwf.widgets.basic.PreformatedTextLabel;
import net.magik6k.jwwf.widgets.basic.TextLabel;
import net.magik6k.jwwf.widgets.basic.panel.Panel;
import net.magik6k.mpt.db.UserBase;
import net.magik6k.mpt.panel.UserPanel;
import net.magik6k.mpt.util.IllegalUserActionException;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

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
		getUserData().set("__jwwf.oauth.code","");
		getUserData().set("__jwwf.oauth.token","");
		viewLoginPanel();
	}
	
	public UserAuth getAuth(){
		return auth;
	}
	
	public void handleCriticalError(Exception ex){
		TextLabel fail = new TextLabel("User has performed illegal action and/or an error ocured:");
		PreformatedTextLabel exceptionLabel = new PreformatedTextLabel(ex.getMessage());
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		PreformatedTextLabel stackLabel = new PreformatedTextLabel(sw.toString());
		rootFrame.put(new Panel(3, fail, exceptionLabel, stackLabel));
	}

	private void viewLoginPanel(){
		final MptClient clinetInstance = this;
		
		Panel frontPanel = new Panel(4);
		
		frontPanel.put(new TextLabel("<h2>Welcome to MPTv2 web IDE</h2>"));
		frontPanel.put(new TextLabel("To login simply click the login button. You may help making this place better by reporting issues on "));
		frontPanel.put(new ExternalLink("https://github.com/magik6k/MPT2", "the GitHub project page"));
		
		frontPanel.put(new OAuth2Button("Login with GitHub", Settings.instance.getProperty("githubClientID")
				, Settings.instance.getProperty("githubClientSecret")
				, "https://github.com/login/oauth/authorize", "", Settings.instance.getProperty("githubAuthRedirURL")
				, "https://github.com/login/oauth/access_token", new OAuthHandler() {
					
					@Override
					public void authorized(String accessToken) {
						rootFrame.put(new TextLabel("Please wait.. Checking your identity"));
						GitHubClient client = new GitHubClient();
						client.setOAuth2Token(accessToken);
						UserService service = new UserService(client);
						try {
							org.eclipse.egit.github.core.User user = service.getUser();
							if(!UserBase.instance.userExists(user.getLogin()));
								try {
									UserBase.instance.register(user.getLogin());
								} catch (IllegalUserActionException e) {
									handleCriticalError(e);
								}
							
							auth.loginAs(user.getLogin());
							userPanel = new UserPanel(clinetInstance);
							rootFrame.put(userPanel);
						} catch (IOException e) {
							handleCriticalError(e);
						}
					}
				}));
		
		rootFrame.put(frontPanel);
	}
}
