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
