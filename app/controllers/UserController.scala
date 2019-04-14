package controllers

import dao._
import javax.inject._
import models._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

class UserController @Inject()(userDAO: UserDAO,
                               cc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

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
}
