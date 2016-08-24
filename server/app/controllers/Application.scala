package controllers

import java.net.URLDecoder
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global

class Application @Inject() (ws: WSClient, conf: Configuration) extends Controller {
  private def redirectAuthGithub() = {
    Redirect(s"https://github.com/login/oauth/authorize?client_id=${conf.getString("mpt.github.id").head}&redirect_uri=${conf.getString("mpt.github.redir").head}&scope=")
  }

  def index = Action { request =>
    val login = request.session.get("login")
    if(login.isDefined)
      Ok(views.html.index())
    else
      redirectAuthGithub()
  }

  def page(page: String) = Action { request =>
    val login = request.session.get("login")
    if(login.isDefined)
      Ok(views.html.index())
    else
      redirectAuthGithub()
  }

  def logout = Action { request =>
    Redirect("/").withSession()
  }

  //AUTH

  def authGithub(c: Option[String]) = Action.async {
    c match {
      case Some(code: String) =>
        val request = ws.url(s"https://github.com/login/oauth/access_token?code=$code&client_id=${conf.getString("mpt.github.id").head}&client_secret=${conf.getString("mpt.github.secret").head}")
        val resp: Future[Result] = request.get().map { response =>
          val rmap = response.body.split("&").map(_.split("=", 2)).map(e => (e(0), URLDecoder.decode(e(1), "UTF-8"))).toMap
          if(rmap.contains("access_token")) {
            val gh_req = ws.url("https://api.github.com/user").withHeaders("Authorization" -> s"token ${rmap("access_token")}")
            val r = gh_req.get().map { response =>
              Redirect("/").withSession("login" -> (response.json \ "login").as[String], "ssid" -> UUID.randomUUID().toString)
            }
            Await.result(r, Duration(10, TimeUnit.SECONDS))
          } else
            Redirect("/error/auth")
        }
        resp
      case _ => Future { Status(400) }
    }

  }

  //TABS

  def profile = Action {
    Ok(views.html.profile.profile())
  }

  def repositories = Action {
    Ok(views.html.repository.repositories())
  }

  def repository = Action {
    Ok(views.html.repository.repository())
  }

  def pack = Action {
    Ok(views.html.repository.pack())
  }

  def file = Action {
    Ok(views.html.repository.file())
  }
}
