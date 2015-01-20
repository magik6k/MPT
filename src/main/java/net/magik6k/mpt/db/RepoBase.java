package net.magik6k.mpt.db;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class RepoBase {
	public static RepoBase instance;
	
	private DBCollection repos;
	
	public RepoBase(){
		repos = BaseController.instance.getCollection("repos");
	}
	
	public boolean exists(String repo){
		 return repos.findOne(new BasicDBObject().append("name", repo)) != null;
	}
	
	public void addRepo(String name, String owner){
		BasicDBObject repo = new BasicDBObject();
		repo.put("name", name);
		repo.put("owner", owner);
		repo.put("users", new String[]{owner});
		
		repos.insert(repo);
	}
	
	public boolean isOwner(String user, String repoo){
		BasicDBObject repo = (BasicDBObject) repos.findOne(new BasicDBObject().append("name", repoo));
		return repo.get("owner").equals(user);
	}
	
	public boolean hasUser(String user, String repo){
		DBObject rep = repos.findOne(new BasicDBObject().append("name", repo));
		BasicDBList users = (BasicDBList)rep.get("users");
		
		for(Object u : users){
			if(((String)u).equals(user)){
				return true;
			}
		}
		return false;
	}
	
	public void addUser(String repo, String user){
		DBObject rep = repos.findOne(new BasicDBObject().append("name", repo));
		BasicDBList users = (BasicDBList)rep.get("users");
		users.add(user);
		repos.update(new BasicDBObject().append("name", repo), rep);
	}
	
	public void removeUser(String repo, String user){
		DBObject rep = repos.findOne(new BasicDBObject().append("name", repo));
		BasicDBList users = (BasicDBList)rep.get("users");
		BasicDBList newUsers = new BasicDBList();
		
		for(Object dep : users){
			if(!((String)dep).equals(user)){
				newUsers.add(dep);
			}
		}
		rep.put("users", newUsers);
		repos.update(new BasicDBObject().append("name", repo), rep);
	}
	
	public int getUserRepoCount(String user){
		int c = 0;
		for(DBObject repo: repos.find()){
			if(repo.containsField("users")){
				Object userList = repo.get("users");
				for(Object name : (BasicDBList) userList){
					if(((String) name).equals(user)){
						++c;
					}
				}
			}
		}
		return c;
	}
	
	public List<String> getRepoUsers(String repoo){
		List<String> res = new LinkedList<String>();
		for(DBObject repo: repos.find(new BasicDBObject().append("name", repoo))){
			if(repo.containsField("users")){
				Object userList = repo.get("users");
				for(Object name : (BasicDBList) userList){
					res.add((String) name);
				}
			}
		}
		return res;
	}
	
	public List<String> getUserRepos(String user){
		List<String> res = new LinkedList<String>();
		for(DBObject repo: repos.find()){
			if(repo.containsField("users")){
				Object userList = repo.get("users");
				for(Object name : (BasicDBList) userList){
					if(((String) name).equals(user)){
						res.add((String) repo.get("name"));
					}
				}
			}
		}
		return res;
	}
}
