package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's profile page.
 */
@Singleton
class ProfileController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Action to create Profile HTML file
   */
  def profile() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map {
      user => Ok(views.html.profile())}.getOrElse {
      Unauthorized("You are not connected")
    }
  }

  def logout() = Action { implicit  request =>
    Redirect(routes.LoginController.login).withNewSession
  }

}
