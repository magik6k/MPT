package models

import com.mongodb.{BasicDBList, BasicDBObject, MongoClient}
import models.util.VersionUtil

import scala.collection.mutable
import collection.JavaConverters._

object Database {
  private val mongo = new MongoClient("localhost", 27017)
  private val database = mongo.getDB("mpt")

  private val repositories = database.getCollection("repos")
  private val packages = database.getCollection("packages")

  def userRepositories(user: String): mutable.Buffer[String] = {
    repositories.find(new BasicDBObject().append("users", user))
      .toArray.asScala.map(_.get("name")).asInstanceOf[mutable.Buffer[String]]
  }

  def repoExists(repo: String): Boolean = {
    repositories.find(new BasicDBObject().append("name", repo)).count() > 0
  }

  def createRepo(name: String, owner: String): Unit = {
    val repo = new BasicDBObject()
    repo.put("name", name)
    repo.put("owner", owner)
    repo.put("users", Array[String]{owner})

    repositories.insert(repo)
  }

  def repoPackages(repo: String): mutable.Buffer[String] = {
    packages.find(new BasicDBObject().append("repo", repo))
      .toArray.asScala.map(_.get("name")).asInstanceOf[mutable.Buffer[String]]
  }

  def repoOwner(repo: String) = {
    repositories.findOne(new BasicDBObject().append("name", repo)).asInstanceOf[BasicDBObject].getString("owner")
  }

  def isRepoUser(repo: String, user: String): Boolean = {
    repositories.findOne(new BasicDBObject().append("name", repo))
      .asInstanceOf[BasicDBObject].get("users").asInstanceOf[BasicDBList].contains(user)
  }

  def repoUsers(repo: String) = {
    val users = repositories.findOne(new BasicDBObject().append("name", repo)).get("users").asInstanceOf[BasicDBList]
    users.toArray(new Array[String](users.size()))
  }

  def addRepoUser(repo: String, user: String): Unit = {
    val r = repositories.findOne(new BasicDBObject().append("name", repo))
    val users = r.get("users").asInstanceOf[BasicDBList]
    users.add(user)
    repositories.update(new BasicDBObject().append("name", repo), r)
  }

  def removeRepoUser(repo: String, user: String): Unit = {
    val r = repositories.findOne(new BasicDBObject().append("name", repo))
    val users = r.get("users").asInstanceOf[BasicDBList]
    val newUsers = new BasicDBList
    import scala.collection.JavaConversions._
    users.map(u => u.asInstanceOf[String]).filterNot(_.equals(user)).foreach(newUsers.add)
    r.put("users", newUsers)
    repositories.update(new BasicDBObject().append("name", repo), r)
  }

  /////////////////////

  def createPackage(name: String, repo: String): Unit = {
    val pack = new BasicDBObject()
    pack.put("name", name)
    pack.put("repo", repo)
    pack.put("files", new BasicDBObject())
    pack.put("checksum", VersionUtil.getNewHash(name))

    val stats = new BasicDBObject()
    stats.put("views", 0)
    stats.put("installations", 0)
    stats.put("size", 0)
    stats.put("files", 0)

    pack.put("stats", stats)
    pack.put("dependencies", new BasicDBList())

    packages.insert(pack)
  }

  def packageExists(name: String) = {
    packages.find(new BasicDBObject().append("name", name)).count() > 0
  }

  def packageRepo(name: String) = {
    packages.findOne(new BasicDBObject().append("name", name)).asInstanceOf[BasicDBObject].getString("repo")
  }

  def packageFiles(pack: String): mutable.Set[String] = {
    packages.find(new BasicDBObject().append("name", pack))
      .toArray.asScala.map(_.get("files").asInstanceOf[BasicDBObject]).head.keySet().asScala.map(unsafeFile)
  }

  def packageDependencies(pack: String) = {
    val deps = packages.findOne(new BasicDBObject().append("name", pack)).asInstanceOf[BasicDBObject].get("dependencies").asInstanceOf[BasicDBList]
    deps.toArray(new Array[String](deps.size()))
  }

  def removeDependency(pack: String, dep: String) = {
    val p = packages.findOne(new BasicDBObject().append("name", pack))
    val dependencies = p.get("dependencies").asInstanceOf[BasicDBList]
    val newDependencies = new BasicDBList
    import scala.collection.JavaConversions._
    dependencies.map(u => u.asInstanceOf[String]).filterNot(_.equals(dep)).foreach(newDependencies.add)
    p.put("dependencies", newDependencies)
    packages.update(new BasicDBObject().append("name", pack), p)
  }

  def addDependency(pack: String, dep: String) = {
    val p = packages.findOne(new BasicDBObject().append("name", pack))
    val dependencies = p.get("dependencies").asInstanceOf[BasicDBList]
    dependencies.add(dep)
    packages.update(new BasicDBObject().append("name", pack), p)
  }

  def getPackageFile(pack: String, file: String): String = {
    val files = packages.findOne(new BasicDBObject().append("name", pack)).get("files").asInstanceOf[BasicDBObject]
    if(files.containsField(safeFile(file)))
      files.get(safeFile(file)).asInstanceOf[BasicDBObject].getString("content")
    else
      null
  }

  def fileExists(pack: String, file: String): Boolean = {
    packages.findOne(new BasicDBObject().append("name", pack)).get("files").asInstanceOf[BasicDBObject].get(safeFile(file)) != null
  }

  def createFile(pack: String, file: String): Unit = {
    val dbpack = packages.findOne(new BasicDBObject().append("name", pack))
    val files = dbpack.get("files").asInstanceOf[BasicDBObject]
    val f = new BasicDBObject("content", "\n")
    files.put(safeFile(file), f)
    dbpack.put("files", files)
    packages.update(new BasicDBObject().append("name", pack), dbpack)
    recalculatePackageChecksum(pack)
  }

  def removeFile(pack: String, file: String): Unit = {
    val dbpack = packages.findOne(new BasicDBObject().append("name", pack))
    val files = dbpack.get("files").asInstanceOf[BasicDBObject]
    files.remove(file)
    dbpack.put("files", files)
    packages.update(new BasicDBObject().append("name", pack), dbpack)
    recalculatePackageChecksum(pack)
  }

  def saveFile(pack: String, file: String, content: String): Unit = {
    val dbpack = packages.findOne(new BasicDBObject().append("name", pack))
    val files = dbpack.get("files").asInstanceOf[BasicDBObject]
    val f = new BasicDBObject("content", content)
    files.put(safeFile(file), f)
    dbpack.put("files", files)
    packages.update(new BasicDBObject().append("name", pack), dbpack)
    recalculatePackageChecksum(pack)
  }

  def packageSummary(name: String) = {
    val pack = packages.findOne(new BasicDBObject().append("name", name)).asInstanceOf[BasicDBObject]
    (pack.getString("repo"), pack.getString("checksum"))
  }

  def recalculatePackageChecksum(pack: String): Unit = {
    val sum = VersionUtil.getPackageHash(pack)
    val update = packages.findOne(new BasicDBObject().append("name", pack))
    update.put("checksum", sum)
    packages.update(new BasicDBObject().append("name", pack), update)
  }

  private def safeFile(file: String): String = {
    file.replace("\\", "\\\\").replace(".", "\\_")
  }

  private def unsafeFile(file: String): String = {
    file.replace("\\_", ".").replace("\\\\", "\\")
  }
}
