package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's profile page.
 */
@Singleton
class LoginController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Action to create Login HTML file
   */
  def login() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

}