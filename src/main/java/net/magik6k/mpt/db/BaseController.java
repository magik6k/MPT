package net.magik6k.mpt.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class BaseController {
	public static final BaseController instance = new BaseController();
	
	private final MongoClient mongo;
	private DB db;
	
	private BaseController() {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.mongo = mongo;
		db = mongo.getDB("mpt");
	}
	
	public DBCollection getCollection(String name){
		return db.getCollection(name);
	}
}
