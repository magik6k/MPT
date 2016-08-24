package controllers

import models.Database
import play.api.mvc.{Action, Controller}

class PublicApi extends Controller {
  def getFile(pack: String, file: String) = Action {
    Ok(Database.getPackageFile(pack, "/" + file))
  }
}
