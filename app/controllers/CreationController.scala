package controllers

import play.api.Logger
import javax.inject._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n._

case class UserData(firstname: String, lastname: String, phone: String, password: String, email: String)

class CreationController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  val creationLogger: Logger = Logger("creation")

  val userForm = Form(
    mapping(
      "firstname" -> nonEmptyText,
      "lastname"  -> nonEmptyText,
      "phone" -> nonEmptyText,
      "password" -> text(minLength = 8),
      "email" -> email
    )(UserData.apply)(UserData.unapply)
  )

  def work = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.creationWork(userForm, postUrl))
  }

  private val postUrl = routes.CreationController.createUser()

  def createUser: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[UserData] =>
      BadRequest(views.html.creationWork(formWithErrors, postUrl))
    }

    val successFunction = { userData: UserData =>
      creationLogger.info("User Processed Successfully")
      val user = UserData(
        firstname = userData.firstname,
        lastname = userData.lastname,
        phone = userData.phone,
        password = userData.password,
        email = userData.email)
      Redirect(routes.CreationController.work())
    }

    val formValidationResult = userForm.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def hire(): Action[AnyContent] = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.creationHire())
  }
}
