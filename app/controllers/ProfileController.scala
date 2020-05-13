package controllers

import java.io.File
import java.nio.file.Paths
import java.util.Locale

import javax.inject._
import play.api._
import play.api.mvc._
import play.filters._


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

  //Ideally we want to use the app's login controller to redirect from the profile page to the login page
  //It should be changed once login controller is available
  //HomeController was used for testing this feature, but it is not important

  def upload = Action(parse.multipartFormData) { request =>
    request.body
      .file("picture")
      .map { picture =>
        val filename = picture.filename
        val contentType = picture.contentType
        var replace = contentType.contains("image/png") || contentType.contains("image/jpeg")
        picture.ref.copyTo(new File(s"public/images/$filename"))
        Redirect(routes.ProfileController.profile()).flashing("Success" -> "Successful upload")
      }
      .getOrElse {
        Redirect(routes.ProfileController.profile()).flashing("error" -> "Missing file")
      }
  }
}
