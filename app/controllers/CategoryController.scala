package controllers

import dao._
import javax.inject._
import models._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class CategoryController @Inject() (categoryDAO: CategoryDAO,
                                    cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getCategories = Action.async { implicit request =>
    categoryDAO.all().map{ category =>
      Ok(Json.toJson(category))
    }
  }

  def getCategoryById(categoryID: Long) = Action.async { implicit request =>
    categoryDAO.getById(categoryID).map{ category =>
      Ok(Json.toJson(category))
    }
  }
}
