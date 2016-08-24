package controllers

import models.Database
import play.api.mvc._
import play.api.libs.json._

class Api extends Controller {
  // Internal
  def addRepository() = Action { request =>
    request.body.asJson match {
      case Some(repo: JsString) =>
        if(Database.repoExists(repo.value))
          Status(400)
        else
          request.session.get("login") match {
            case Some(login: String) =>
              Database.createRepo(repo.value, login)
              Ok("true")
            case _ => Status(400)
          }
      case _ => Status(400)
    }
  }

  def addPackage() = Action { request =>
    request.body.asJson match {
      case Some(data: JsObject) =>
        val repo = data.value.get("repo").head.asInstanceOf[JsString].value
        val pack = data.value.get("package").head.asInstanceOf[JsString].value

        if(!Database.repoExists(repo) || Database.packageExists(pack)) {
          Status(400)
        } else
          request.session.get("login") match {
            case Some(login: String) =>
              if (Database.repoOwner(repo).equals(login)) {
                Database.createPackage(pack, repo)
                Ok("true")
              } else Status(400)
            case _ => Status(400)
          }
      case _ => Status(400)
    }
  }

  def addRepositoryUser() = Action { request =>
    request.body.asJson match {
      case Some(data: JsObject) =>
        val repo = data.value.get("repo").head.asInstanceOf[JsString].value
        val user = data.value.get("collaborator").head.asInstanceOf[JsString].value

        if(!Database.repoExists(repo) || Database.isRepoUser(repo, user)) {
          Status(400)
        } else
          request.session.get("login") match {
            case Some(login: String) =>
              if (Database.repoOwner(repo).equals(login)) {
                Database.addRepoUser(repo, user)
                Ok("true")
              } else Status(400)
            case _ => Status(400)
          }
      case _ => Status(400)
    }
  }

  def delRepositoryUser() = Action { request =>
    request.body.asJson match {
      case Some(data: JsObject) =>
        val repo = data.value.get("repo").head.asInstanceOf[JsString].value
        val user = data.value.get("collaborator").head.asInstanceOf[JsString].value

        if(!Database.repoExists(repo) || !Database.isRepoUser(repo, user) || Database.repoOwner(repo).equals(user)) {
          Status(400)
        } else
          request.session.get("login") match {
            case Some(login: String) =>
              if (Database.repoOwner(repo).equals(login)) {
                Database.removeRepoUser(repo, user)
                Ok("true")
              } else Status(400)
            case _ => Status(400)
          }
      case _ => Status(400)
    }
  }

  def createFile(pack: String) = Action { request =>
    request.session.get("login") match {
      case Some(login: String) =>
        if(Database.packageExists(pack) && Database.isRepoUser(Database.packageRepo(pack), login)) {
          request.body.asJson match {
            case Some(data: JsObject) =>
              val f = data.value.get("file").head.asInstanceOf[JsString].value
              if (!Database.fileExists(pack, f))
                Database.createFile(pack, f)
              Ok("true")
            case _ => Status(400)
          }
        }
        else
          Status(400)
      case _ => Status(400)
    }
  }

  def saveFile(pack: String) = Action { request =>
    request.session.get("login") match {
      case Some(login: String) =>
        if(Database.packageExists(pack) && Database.isRepoUser(Database.packageRepo(pack), login)) {
          request.body.asJson match {
            case Some(data: JsObject) =>
              val f = data.value.get("file").head.asInstanceOf[JsString].value
              val c = data.value.get("content").head.asInstanceOf[JsString].value
              if (Database.fileExists(pack, f))
                Database.saveFile(pack, f, c)
              Ok("true")
            case _ => Status(400)
          }
        }
        else
          Status(400)
      case _ => Status(400)
    }
  }

  def deleteFile(pack: String) = Action { request =>
    request.session.get("login") match {
      case Some(login: String) =>
        if(Database.packageExists(pack) && Database.isRepoUser(Database.packageRepo(pack), login)) {
          request.body.asJson match {
            case Some(data: JsObject) =>
              val f = data.value.get("file").head.asInstanceOf[JsString].value
              if (Database.fileExists(pack, f))
                Database.removeFile(pack, f)
              Ok("true")
            case _ => Status(400)
          }
        }
        else
          Status(400)
      case _ => Status(400)
    }
  }

  def repoExists(repo: String) = Action { request =>
    Ok(JsBoolean(Database.repoExists(repo)))
  }

  def userRepositories(user: String) = Action {
    val res = JsArray(Database.userRepositories(user).map(JsString).toSeq)
    Ok(res)
  }

  def repoPackages(repo: String) = Action {
    val res = JsArray(Database.repoPackages(repo).map(JsString).toSeq)
    Ok(res)
  }

  def repoUsers(repo: String) = Action {
    val res = JsArray(Database.repoUsers(repo).map(JsString).toSeq)
    Ok(res)
  }

  def packageFiles(pack: String) = Action {
    val res = JsArray(Database.packageFiles(pack).map(JsString).toSeq)
    Ok(res)
  }

  def packageDependencies(pack: String) = Action {
    val res = JsArray(Database.packageDependencies(pack).map(JsString).toSeq)
    Ok(res)
  }

  def packageDepAdd(pack: String) = Action { request =>
    request.session.get("login") match {
      case Some(login: String) =>
        if(Database.packageExists(pack) && Database.isRepoUser(Database.packageRepo(pack), login)) {
          request.body.asJson match {
            case Some(data: JsObject) =>
              val dep = data.value.get("package").head.asInstanceOf[JsString].value
              if (!Database.packageDependencies(pack).contains(dep))
                Database.addDependency(pack, dep)
              Ok("true")
            case _ => Status(400)
          }
        }
        else
          Status(400)
      case _ => Status(400)
    }
  }

  def packageDepDelete(pack: String) = Action { request =>
    request.session.get("login") match {
      case Some(login: String) =>
        if(Database.packageExists(pack) && Database.isRepoUser(Database.packageRepo(pack), login)) {
          request.body.asJson match {
            case Some(data: JsObject) =>
              val dep = data.value.get("package").head.asInstanceOf[JsString].value
              if (Database.packageDependencies(pack).contains(dep))
                Database.removeDependency(pack, dep)
              Ok("true")
            case _ => Status(400)
          }
        }
        else
          Status(400)
      case _ => Status(400)
    }
  }

  def profile() = Action { request =>
    request.session.get("login") match {
      case Some(login: String) => Ok(JsObject(Seq("name" -> JsString(login))))
      case _ => Ok("{}")
    }
  }
  // Public

  def notFound(any: String) = Action { request =>
    Status(NOT_FOUND)
  }

}
