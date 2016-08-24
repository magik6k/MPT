package controllers

import models.Database
import play.api.mvc.{Action, Controller}

class PublicApi extends Controller {
  def getFile(pack: String, file: String) = Action {
    Ok(Database.getPackageFile(pack, "/" + file))
  }

  def getPackage(pack: String) = Action {
    val (repo: String, checksum: String) = Database.packageSummary(pack)
    Ok(s"""{name="$pack",repo="$repo",checksum="$checksum",files={${
      Database.packageFiles(pack).map(f => s""""$f"""").mkString(",")
    }},dependencies={${
      Database.packageDependencies(pack).map(f => s""""$f"""").mkString(",")
    }}}""")
  }

  def getUpdate = Action { request =>
    Ok(
      s"""{${
        request.body.asFormUrlEncoded.head.map{
          case (pack: String, sum: Seq[String]) =>
            if(Database.packageExists(pack)) {
              val dbSum = Database.packageSummary(pack)._2
              if (!dbSum.equals(sum.head))
                s"""{package="$pack",checksum="$dbSum"}"""
              else null
            } else s"""{package="$pack",checksum=nil}"""
          case _ => null
        }.filter(_ != null).mkString(",")
      }"""
    )
  }
}
