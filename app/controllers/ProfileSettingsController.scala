package controllers

import javax.inject._

import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProfileSettingsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Action to create Profile HTML file
   */
  def profileSettings() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map {
      user => Ok(views.html.profilesettings())}.getOrElse {
      Unauthorized("You are not connected")
    }  }

}