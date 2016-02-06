package models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

object GlobalModules {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
}
