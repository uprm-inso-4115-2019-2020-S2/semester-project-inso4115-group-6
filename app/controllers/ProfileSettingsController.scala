package controllers

import javax.inject._
import play.api.mvc._
import _root_.Handler.ProfileHandler

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProfileSettingsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  val profile = new ProfileHandler()
  /**
   * Action to create Profile HTML file
   */
  def profileSettings() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.profilesettings())
  }

  def updateDescription(desc: String): Unit ={
    //profile.updateServiceDescription(desc -> )
  }

}