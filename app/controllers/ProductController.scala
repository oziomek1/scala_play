package controllers

import dao._
import javax.inject._
import models._
import play.api.data.format.Formats._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._
import scala.util.{Failure, Success}

import scala.concurrent.{ExecutionContext, Future}

class ProductController @Inject() (productDAO: ProductDAO,
                                   categoryDAO: CategoryDAO,
                                   cc: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "productName" -> nonEmptyText.verifying(minLength(3), maxLength(50)),
      "productDescription" -> nonEmptyText,
      "categoryID" -> longNumber,
      "productPriceNet" -> of(doubleFormat),
      "productPriceGross" -> of(doubleFormat),
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  def index = Action.async { implicit request =>
    val categories = categoryDAO.all()
    categories.map(cat => Ok(views.html.indexProduct(productForm, cat)))
  }

  def getProducts = Action.async { implicit request =>
    productDAO.all().map{ product =>
      Ok(Json.toJson(product))
    }
  }

  def getProductById(id: Long) = Action.async { implicit request =>
    productDAO.getById(id).map{ product =>
      Ok(Json.toJson(product))
    }
  }

  def getByCategory(id: Long) = Action.async{ implicit request =>
    productDAO.getByCategory(id).map { product =>
      Ok(Json.toJson(product))
    }
  }

  def addProduct = Action.async { implicit request =>
    var a:Seq[Category] = Seq[Category]()
    val categories = categoryDAO.all().onComplete{
      case Success(cat) => a= cat
      case Failure(_) => print("fail")
    }
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.indexProduct(errorForm, a)))
      },
      product => {
        productDAO.create(product.productName, product.productDescription, product.categoryID, product.productPriceNet, product.productPriceGross).map {
          _ => Redirect(routes.ProductController.index).flashing("success" -> "product.created")
        }
      }
    )
  }

  def handlePost = Action.async { implicit request =>
    val productName = request.body.asJson.get("productName").as[String]
    val productDescription = request.body.asJson.get("productDescription").as[String]
    val categoryID = request.body.asJson.get("categoryID").as[Long]
    val productPriceNet = request.body.asJson.get("productPriceNet").as[Double]
    val productPriceGross = request.body.asJson.get("productPriceGross").as[Double]

    productDAO.create(productName, productDescription, categoryID, productPriceNet, productPriceGross).map {
      product => Ok(Json.toJson(product))
    }
  }
}

/**
  * The create person form.
  *
  * Generally for forms, you should define separate objects to your models, since forms very often need to present data
  * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
  * that is generated once it's created.
  */
case class CreateProductForm(productName: String, productDescription: String, categoryID: Long,
                             productPriceNet: Double, productPriceGross: Double)