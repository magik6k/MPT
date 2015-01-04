package net.magik6k.mpt.db;

import net.magik6k.mpt.util.IllegalUserActionException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class UserBase {
	public static UserBase instance;
	
	private DBCollection users;
	
	public UserBase(){
		users = BaseController.instance.getCollection("users");
	}
	
	public void register(String username, String password) throws IllegalUserActionException{
		if(userExists(username))throw new IllegalUserActionException("User is already registered!");
		
		BasicDBObject user = new BasicDBObject();
		user.put("name", username);
		user.put("password", password);
		users.insert(user);
	}
	
	public boolean userExists(String user){
		 return users.findOne(new BasicDBObject().append("name", user)) != null;
	}
	
	public boolean userMatches(String user, String password){
		 return users.findOne(new BasicDBObject().append("name", user).append("password", password)) != null;
	}
	
}
