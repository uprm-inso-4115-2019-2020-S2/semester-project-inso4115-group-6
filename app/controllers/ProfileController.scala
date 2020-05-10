package controllers

import java.io.File
import java.nio.file.Paths
import java.util.Locale

import javax.inject._
import models.userModel
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
  def profile() = Action { implicit request  =>
    Ok(views.html.profile())
  }

  def update = Action(parse.multipartFormData) {
    implicit request =>
    request.body.file("picture").map { picture =>
        val filename    = "ProfilePic.png"
        val filename1    = Paths.get(picture.filename).getFileName
        val fileSize    = picture.fileSize
        val contentType = picture.contentType
//        var replace =  picture.contentType.contains("image/png")

        picture.ref.copyTo(Paths.get(s"home/jd/semester-project-inso4115-group-6/public/images/$filename"), replace = true)
        Redirect(routes.ProfileController.profile())
        }
      .getOrElse {
        Redirect(routes.ProfileController.profile()).flashing("error" -> "Missing file")
      }
  }

  def upload=  Action(parse.multipartFormData) {
    implicit request =>
      request.body.file("picture").map { picture =>
        val filename = "Pic1"
        val contentType = picture.contentType.get
        picture.ref.moveTo(new File("home/jd/semester-project-inso4115-group-6/public/images/" + filename))
        Redirect(routes.ProfileController.profile())
      }.getOrElse {
        Ok("ERROR404")
      }
  }
}
