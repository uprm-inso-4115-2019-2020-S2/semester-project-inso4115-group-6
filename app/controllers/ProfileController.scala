package controllers

import java.nio.file.Paths

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

  def upload = Action(parse.multipartFormData) { request =>
    request.body
      .file("picture")
      .map { picture =>
        // only get the last part of the filename
        // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
        val filename    = Paths.get(picture.filename).getFileName
        val fileSize    = picture.fileSize
        val contentType = picture.contentType
        picture.ref.copyTo(Paths.get(s"/public/images/$filename"), replace = true)
        Ok("File uploaded")
      }
      .getOrElse {
        Ok("ERROR404")
      }
  }

}
