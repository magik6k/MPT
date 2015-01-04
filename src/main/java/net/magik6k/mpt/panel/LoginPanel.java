package net.magik6k.mpt.panel;

import net.magik6k.jwwf.enums.PanelAlign;
import net.magik6k.jwwf.handlers.ClickHandler;
import net.magik6k.jwwf.widgets.basic.input.Button;
import net.magik6k.jwwf.widgets.basic.input.PasswordInput;
import net.magik6k.jwwf.widgets.basic.input.TextInput;
import net.magik6k.jwwf.widgets.basic.panel.HorizontalPanel;
import net.magik6k.jwwf.widgets.basic.panel.VerticalPanel;
import net.magik6k.mpt.MptClient;
import net.magik6k.mpt.action.LoginHandler;
import net.magik6k.mpt.db.UserBase;
import net.magik6k.mpt.plugin.FadeOutLabel;
import net.magik6k.mpt.util.IllegalUserActionException;

public class LoginPanel extends VerticalPanel{

	private final TextInput loginField;
	private final PasswordInput passwordField;
	private final Button loginButton, registerButton;
	
	public LoginPanel(final LoginHandler handler, final MptClient owner) {
		super(4);
		this.setElementSpacing(4);
		this.setElementAlign(PanelAlign.MIDDLE);
		loginField = new TextInput("login");
		passwordField = new PasswordInput("password");
		
		
		loginButton = new Button("Login", new ClickHandler() {
			@Override
			public void clicked() {
				try {
					checkFields();
					if(UserBase.instance.userMatches(loginField.getText(), passwordField.getText())){
						handler.login(loginField.getText(), passwordField.getText());
					}else{
						throw new IllegalUserActionException("Bad username or passoword");
					}
				} catch (IllegalUserActionException e) {
					owner.handleCriticalError(e);
				}
				
				
			}
		});
		
		registerButton = new Button("Register", new ClickHandler() {
			@Override
			public void clicked() {
				try {
					checkFields();
					UserBase.instance.register(loginField.getText(), passwordField.getText());
				} catch (IllegalUserActionException e) {
					owner.handleCriticalError(e);
				}
				handler.login(loginField.getText(), passwordField.getText());
			}
		});
		
		this.put(loginField);
		this.put(passwordField);
		this.put(new HorizontalPanel(2, loginButton, registerButton));
		this.put(new FadeOutLabel("Welcome to MPT2 system!"));
	}
	
	private void checkFields() throws IllegalUserActionException{
		if(loginField.getText().length() < 2||passwordField.getText().length() < 2)
			throw new IllegalUserActionException("Username or passoword too short");
	}
	
}
