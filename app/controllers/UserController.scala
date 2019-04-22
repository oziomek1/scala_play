package controllers

import dao._
import javax.inject._
import models._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.data.format.Formats._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(userDAO: UserDAO,
                               cc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "userEmail" -> nonEmptyText.verifying(minLength(6), maxLength(50)),
      "userPassword" -> nonEmptyText.verifying(minLength(8)),
      "userFirstname" -> text,
      "userLastname" -> text,
      "userAddress" -> text,
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  def index = Action { implicit request =>
    Ok(views.html.indexUser(userForm))
  }

  def getUsers = Action.async { implicit request =>
    userDAO.all().map{ user =>
      Ok(Json.toJson(user))
    }
  }

  def getUserById(id: Long) = Action.async { implicit request =>
    userDAO.getById(id).map{ user =>
      Ok(Json.toJson(user))
    }
  }

  def addUser = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok("ERROR"))
      },
      user => {
        userDAO.create(user.userEmail, user.userPassword, user.userFirstname, user.userLastname, user.userAddress).map {
          _ => Redirect(routes.UserController.index).flashing("success" -> "user.created")
        }
      }
    )
  }
}

case class CreateUserForm(userEmail: String, userPassword:String, userFirstname: String, userLastname: String, userAddress: String)
