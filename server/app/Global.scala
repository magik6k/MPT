import models.GlobalModules
import play.{Application, GlobalSettings}

class Global extends GlobalSettings {

  override def onStart(app: Application) {
    println("Start app")
    GlobalModules.init()
  }

  override def onStop(app: Application) {
    println("Stop app")
  }
}
