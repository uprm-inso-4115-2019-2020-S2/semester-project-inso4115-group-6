package controllers

import HandlerLogin.UsersHandler
import javax.inject._
import play.api.data._
import play.api.data.Forms
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n._


case class User(email: String, password: String)

@Singleton
class LoginController @Inject()(cc : MessagesControllerComponents) extends MessagesAbstractController(cc) {


  val userFormData = Form(mapping(
    "email" -> email,
    "password" -> text(minLength = 8))(User.apply)(User.unapply))

  /**
   * Action to create Login HTML file
   */

  // THIS IS MIGUEL VAZQUEZ CODE:::
  // def login = Action { implicit request =>
  //   Ok(views.html.login(userFormData))
  // }

  // def validateLoginUser = Action { implicit request =>
  //   userFormData.bindFromRequest.fold(
  //     formWithErrors => BadRequest(views.html.login(formWithErrors)),
  //     user => Ok("User OK!"))
  // }


  //DERIVED FROM CREATIONCONTROLLER
  def login = Action { implicit request =>
    Ok(views.html.login(userFormData, postUrl))
  }

  private val postUrl = routes.LoginController.validateLoginUser()
  private val handler = UsersHandler()

//  def validateLoginUser = Action { implicit request =>
//    val formValidationResult = userFormData.bindFromRequest()
//    if (formValidationResult.hasErrors) {
//      BadRequest(views.html.login(formValidationResult, postUrl))
//    }
//      val successFunction = { userData: User =>
//        val currentUser = User(
//          email = userData.email,
//          password = userData.password)
//        val buildUserDict = handler.buildLoginDict(Array(currentUser.email, currentUser.password))
//        val confirmUser = handler.login(buildUserDict)
//        if (confirmUser) {
//          Redirect(routes.ProfileController.profile()).withSession("connected" -> currentUser.email)
//        }
//        else {
//          Ok(views.html.login(userFormData, postUrl)).flashing("Incorrect" -> "Credentials are incorrect")
//        }
//      }
//  }
//}

  def validateLoginUser = Action { implicit request =>
  val errorFunction = { formWithErrors: Form[User] =>
    BadRequest(views.html.login(formWithErrors, postUrl))
    }

    val successFunction = { userData: User =>
      val user = User(
        email = userData.email,
        password = userData.password)
      val buildUserDict = handler.buildLoginDict(Array(user.email, user.password))
      val confirmUser = handler.login(buildUserDict)
      if (confirmUser) {
        Redirect(routes.ProfileController.profile()).withSession("connected" -> user.email)
      }
      else {
        Redirect(routes.LoginController.login()).flashing("Incorrect" -> "Credentials are incorrect")
      }
    }

    val formValidationResult = userFormData.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

}