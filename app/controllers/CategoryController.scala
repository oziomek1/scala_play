package controllers

import dao._
import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class CategoryController @Inject() (categoryDAO: CategoryDAO,
                                    cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "categoryName" -> nonEmptyText.verifying(minLength(3), maxLength(50)),
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  def index = Action.async { implicit request =>
    val categories = categoryDAO.all()
    categories.map(cat => Ok(views.html.indexCategory(categoryForm)))
  }

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

  def addCategory = Action.async { implicit request =>
    var a:Seq[Category] = Seq[Category]()
    val categories = categoryDAO.all().onComplete{
      case Success(cat) => a= cat
      case Failure(_) => print("fail")
    }
    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok("ERROR"))
      },
      category => {
        categoryDAO.create(category.categoryName).map {
          _ => Redirect(routes.CategoryController.index).flashing("success" -> "category.created")
        }
      }
    )
  }
}

case class CreateCategoryForm(categoryName: String)