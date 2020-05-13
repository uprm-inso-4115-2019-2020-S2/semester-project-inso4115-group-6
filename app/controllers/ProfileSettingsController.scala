package controllers

import java.awt.image.{BufferedImage, RenderedImage}
import java.io.File

import javax.imageio.ImageIO
import javax.inject._
import play.api.mvc._
import _root_.Handler.ImagesHandler

@Singleton
class ProfileSettingsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val pictureHandler = new ImagesHandler()

  def profileSettings() = Action { implicit request: Request[AnyContent] =>
request.session.get("connected").map {
      user => Ok(views.html.profilesettings())}.getOrElse {
      Unauthorized("You are not connected")
    }  
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.session.get("connected").map {
      user =>
    request.body
      .file("picture")
      .map { picture =>
        val filename = picture.filename
        val contentType = picture.contentType
        var replace = contentType.contains("image/png") || contentType.contains("image/jpeg")
        val output = new File("public/images/" + filename)
        picture.ref.copyTo(new File("public/images/" + filename))
        pictureHandler.uploadImage("public/images/" + filename, user.toInt, false)
        Redirect(routes.ProfileController.profile()).flashing("Success" -> "Successful upload")
      }
      .getOrElse {
        Redirect(routes.ProfileController.profile()).flashing("error" -> "Missing file")
      }
    } .getOrElse{
      Redirect(routes.ProfileController.profile()).flashing("error" -> "Upload unsuccessful")
    }
  }

  def profilePicDelete = Action {
    Ok("TODO")
      }


  def updateProfilePic = Action(parse.multipartFormData) {
    request =>
      request.body.file("picture").map { picture =>
        val filename = "ProfilePic.png"
        val fileSize = picture.fileSize
        val contentType = picture.contentType
        var replace = contentType.contains("image/png") || contentType.contains("image/jpeg")
        picture.ref.copyTo(new File(s"public/images/profile-pictures/$filename"), replace)
        Redirect(routes.ProfileController.profile()).flashing("Success" -> "Successful upload")
      }
        .getOrElse {
          Redirect(routes.ProfileController.profile()).flashing("error" -> "Missing file")
        }
  }
}