package net.magik6k.mpt;

public class UserAuth {
	public String username;
	
	public boolean isLoggedIn(){
		return username != null;
	}
	
	public void loginAs(String name){
		username = name;
	}
	
	public void logout(){
		username = null;
	}
	
	public String getUsername(){
		return username;
	}
}
