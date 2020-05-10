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
    Ok(views.html.profile())
  }

  //Ideally we want to use the app's login controller to redirect from the profile page to the login page
  //It should be changed once login controller is available
  //HomeController was used for testing this feature, but it is not important
  def logout() = Action {
    Redirect(routes.HomeController.index()).withNewSession
  }
}
