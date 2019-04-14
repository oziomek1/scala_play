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

import scala.concurrent.{ExecutionContext, Future}

class ProductController @Inject() (productDAO: ProductDAO,
                                   categoryDAO: CategoryDAO,
                                   cc: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText.verifying(minLength(3), maxLength(50)),
      "description" -> nonEmptyText,
      "category" -> longNumber,
      "price" -> of(floatFormat)
    )(CreateProductForm.apply)(CreateProductForm.unapply)
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

  def addProduct = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm.toString)))
      },
      product => {
        productDAO.create(product.name, product.description, product.category, product.price).map {
          _ => Redirect(routes.HomeController.index)
        }
      }
    )
  }

  def editProduct(id: Long) = Action.async { implicit request =>
    val productToEdit = for {
      product <- productDAO.getById(id)
    } yield (product)

    val updatedProduct: models.Product = productForm.bind(productToEdit).get
    productDAO.update(updatedProduct.id, updatedProduct.name, updatedProduct.description,
      updatedProduct.categoryID, updatedProduct.price)
    Redirect(routes.HomeController.index())
  }

  def deleteProduct(id: Long) = Action.async { implicit request =>
    productDAO.delete(id)
    Redirect(routes.HomeController.index())
  }
}

/**
  * The create person form.
  *
  * Generally for forms, you should define separate objects to your models, since forms very often need to present data
  * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
  * that is generated once it's created.
  */
case class CreateProductForm(name: String, description: String, category: Long, price: Float)