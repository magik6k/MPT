package net.magik6k.mpt.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.magik6k.mpt.util.MptFile;
import net.magik6k.mpt.util.VersionUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class PackageBase {
	public static PackageBase instance;
	
	private DBCollection packages;
	
	public PackageBase(){
		packages = BaseController.instance.getCollection("packages");
	}
	
	//////////////////PACKAGES
	
	public boolean exists(String package_){
		 return packages.findOne(new BasicDBObject().append("name", package_)) != null;
	}
	
	public void addPackage(String name, String repo){
		BasicDBObject pack = new BasicDBObject();
		pack.put("name", name);
		pack.put("repo", repo);
		pack.put("files", new BasicDBObject());
		pack.put("checksum", VersionUtil.getNewHash(name));
		
		BasicDBObject stats = new BasicDBObject();
		stats.put("views", 0);
		stats.put("installations", 0);
		stats.put("size", 0);
		stats.put("files", 0);
		
		pack.put("stats", stats);
		
		BasicDBList dependencies = new BasicDBList();
		pack.put("dependencies", dependencies);
		
		packages.insert(pack);
	}
	
	public void recalculatePackageChecksum(String packagee){
		String sum = VersionUtil.getPackageHash(packagee);
		DBObject update = packages.findOne(new BasicDBObject().append("name", packagee));
		update.put("checksum", sum);
		
		packages.update(new BasicDBObject().append("name", packagee), update);
	}
	
	public List<String> getPackagesForRepo(String repo){
		List<String> res = new LinkedList<String>();
		for(DBObject pack : packages.find(new BasicDBObject().append("repo", repo))){
			res.add((String) pack.get("name"));
		}
		return res;
	}
	
	public void removePackage(String packagee){
		packages.remove(new BasicDBObject().append("name", packagee));
	}
	
	/////////////////////DEPENDENCIES
	
	public boolean hasDependency(String packagee, String dependency){
		DBObject pack = packages.findOne(new BasicDBObject().append("name", packagee));
		BasicDBList dependencies = (BasicDBList)pack.get("dependencies");
		
		for(Object dep : dependencies){
			if(((String)dep).equals(dependency)){
				return true;
			}
		}
		return false;
	}
	
	public void addDependency(String packagee, String dependency){
		DBObject pack = packages.findOne(new BasicDBObject().append("name", packagee));
		BasicDBList dependencies = (BasicDBList)pack.get("dependencies");
		dependencies.add(dependency);
		pack.put("dependencies", dependencies);
		packages.update(new BasicDBObject().append("name", packagee), pack);
		
		recalculatePackageChecksum(packagee);
	}
	
	public void removeDependency(String packagee, String dependency){
		DBObject pack = packages.findOne(new BasicDBObject().append("name", packagee));
		BasicDBList dependencies = (BasicDBList)pack.get("dependencies");
		BasicDBList newDependencies = new BasicDBList();
		
		for(Object dep : dependencies){
			if(!((String)dep).equals(dependency)){
				newDependencies.add(dep);
			}
		}
		pack.put("dependencies", newDependencies);
		packages.update(new BasicDBObject().append("name", packagee), pack);
		
		recalculatePackageChecksum(packagee);
	}
	
	public String[] getDependencies(String packagee){
		DBObject pack = packages.findOne(new BasicDBObject().append("name", packagee));
		BasicDBList dependencies = (BasicDBList)pack.get("dependencies");
		return dependencies.toArray(new String[dependencies.size()]);
	}
	
	////////////////FILES
	
	public boolean fileExists(String package_, String file){
		file = safeFile(file);
		DBObject pack = packages.findOne(new BasicDBObject().append("name", package_));
		if(pack == null)return false;
		return ((BasicDBObject)pack.get("files")).get(file) != null;
	}
	
	public void createFile(String file, String pack_){
		file = safeFile(file);
		DBObject pack = packages.findOne(new BasicDBObject().append("name", pack_));
		BasicDBObject files = (BasicDBObject)pack.get("files");
		
		BasicDBObject f = new BasicDBObject();
		f.put("content", "\n");
		
		files.put(file, f);
		pack.put("files", files);
		packages.update(new BasicDBObject().append("name", pack_), pack);
		
		recalculatePackageChecksum(pack_);
	}
	
	public void removeFile(String file, String pack_){
		file = safeFile(file);
		DBObject pack = packages.findOne(new BasicDBObject().append("name", pack_));
		BasicDBObject files = (BasicDBObject)pack.get("files");
		
		files.remove(file);
		pack.put("files", files);
		packages.update(new BasicDBObject().append("name", pack_), pack);
		
		recalculatePackageChecksum(pack_);
	}
	
	public List<MptFile> getFiles(String pack_){
		DBObject pack = packages.findOne(new BasicDBObject().append("name", pack_));
		BasicDBObject files = (BasicDBObject)pack.get("files");
		
		List<MptFile> res = new LinkedList<>();
		for (Entry<String, Object> f : files.entrySet()) {
			MptFile mf = new MptFile();
			mf.name = unsafeFile(f.getKey());
			mf.pack = pack_;
			mf.content = ((BasicDBObject)f.getValue()).getString("content");
			res.add(mf);
		}
		return res;
	}
	
	public MptFile getFile(String pack_, String file){
		file = safeFile(file);
		DBObject pack = packages.findOne(new BasicDBObject().append("name", pack_));
		BasicDBObject files = (BasicDBObject)pack.get("files");
		
		if(files.containsField(file)){
			MptFile mf = new MptFile();
			mf.name = file;
			mf.pack = pack_;
			mf.content = ((BasicDBObject)files.get(file)).getString("content");
			return mf;
		}

		return null;
	}
	
	public void updateFile(String pack_, String file, String content){
		file = safeFile(file);
		DBObject pack = packages.findOne(new BasicDBObject().append("name", pack_));
		BasicDBObject files = (BasicDBObject)pack.get("files");
		
		BasicDBObject f = new BasicDBObject();
		f.put("content", content);
		
		files.put(file, f);
		pack.put("files", files);
		packages.update(new BasicDBObject().append("name", pack_), pack);
		
		recalculatePackageChecksum(pack_);
	}
	
	//////////////SUMMARY
	
	public PackageSummary getSummary(String packagee){
		BasicDBObject pack = (BasicDBObject) packages.findOne(new BasicDBObject().append("name", packagee));
		return pack != null ? new PackageSummary(packagee, pack.getString("repo"), pack.getString("checksum")) : null;
	}
	
	public static class PackageSummary {
		public final String name;
		public final String repo;
		public final String chechsum;
		
		public PackageSummary(String name, String repo, String chechsum) {
			this.name = name;
			this.repo = repo;
			this.chechsum = chechsum;
		}
	}
	
	private static String safeFile(String file){
		return file.replace("\\", "\\\\").replace(".", "\\_");
	}
	
	private static String unsafeFile(String file){
		return file.replace("\\_", ".").replace("\\\\", "\\");
	}
}
