package controllers

import java.util.UUID

import play.api.mvc._

class Application extends Controller {
  def index = Action { request =>
    val login = request.session.get("login")
    if(login.isDefined)
      Ok(views.html.index())
    else
      Ok(views.html.login())
  }

  def page(page: String) = Action { request =>
    val login = request.session.get("login")
    if(login.isDefined)
      Ok(views.html.index())
    else
      NotFound
  }

  def login = Action { request =>
    Redirect("/").withSession("login" -> "test", "ssid" -> UUID.randomUUID().toString)
  }

  def logout = Action { request =>
    Redirect("/").withSession()
  }

  def profile = Action {
    Ok(views.html.profile.profile())
  }

  def repositories = Action {
    Ok(views.html.repository.repositories())
  }
}
