package controllers

import java.text.SimpleDateFormat
import java.util.Calendar

import dao._
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class OrderController @Inject()(orderDAO: OrderDAO,
                                userDAO: UserDAO,
                                productDAO: ProductDAO,
                                orderDetailDAO: OrderDetailDAO,
                                cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "userID" -> longNumber,
      "orderAddress" -> text.verifying(minLength(6), maxLength(100)),
      "productID" -> longNumber,
      "productQuantity" -> number,
      "orderDetailsPriceNet" -> of(doubleFormat),
      "orderDetailsPriceGross" -> of(doubleFormat),
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def getOrders = Action.async { implicit request =>
    orderDAO.all().map{ order =>
      Ok(Json.toJson(order))
    }
  }

  def getOrderById(id: Long) = Action.async { implicit request =>
    orderDAO.getById(id).map{ order =>
      Ok(Json.toJson(order))
    }
  }

  def getOrderByUser(id: Long) = Action.async { implicit request =>
    orderDAO.getByUserId(id).map { order =>
      Ok(Json.toJson(order))
    }
  }

  def getOrderDetailByOrderId(id: Long) = Action.async{ implicit request =>
    orderDetailDAO.getByOrderId(id).map( { orderDetail =>
      Ok(Json.toJson(orderDetail))
    })
  }

  def addOrder = Action.async { implicit request =>
    var usr:Seq[User] = Seq[User]()
    val users = userDAO.all().onComplete{
      case Success(users) => usr = users
      case Failure(_) => print("fail")
    }

    var prod:Seq[Product] = Seq[Product]()
    val products = productDAO.all().onComplete{
      case Success(products) => prod = products
      case Failure(_) => print("fail")
    }

    val dateFormat = new SimpleDateFormat("YYYY-MM-dd")

    val allUsers = userDAO.all()
    allUsers.map { usr => {
      productDAO.all().map { prod =>
        Ok(views.html.createOrder(orderForm, usr, prod))
        }
      }
    }

    orderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.createOrder(errorForm, usr, prod)))
      },
      order => {
        orderDAO.create(order.userID, order.orderAddress, dateFormat.format(Calendar.getInstance().getTime)).map {
          order_detail =>
            orderDetailDAO.create(order_detail.orderID, order.productQuantity, order.productID, order.orderDetailPriceNet, order.orderDetailPriceGross)
        }.map {
          _ => Redirect(routes.HomeController.index).flashing("success" -> "order.created")
        }
      }
    )
  }

  def deleteOrder(id: Long) = Action.async { implicit request =>
    orderDAO.delete(id).map { order =>
      orderDetailDAO.delete(id)
    }.map {
      _ => Ok(Json.toJson(id))
    }
  }

  def editOrder(id: Long) = Action.async { implicit request =>
    val userID = request.body.asJson.get("userID").as[Long]
    val orderAddress = request.body.asJson.get("orderAddress").as[String]
    val productQuantity = request.body.asJson.get("productQuantity").as[Int]
    val productID = request.body.asJson.get("productID").as[Long]
    val orderDetailsPriceNet = request.body.asJson.get("orderDetailsPriceNet").as[Double]
    val orderDetailsPriceGross = request.body.asJson.get("orderDetailsPriceGross").as[Double]
    val orderDate = request.body.asJson.get("orderDate").as[String]
    val orderShipped = request.body.asJson.get("orderShipped").as[Boolean]
    orderDAO.update(id, userID, orderAddress, orderDate, orderShipped).map { order =>
      orderDetailDAO.update(id, productQuantity, productID, orderDetailsPriceNet, orderDetailsPriceGross)
    }.map {
      orderDetail => Ok(Json.toJson(orderDetail))
    }
  }

  def handlePost = Action.async { implicit request =>
    val userID = request.body.asJson.get("userID").as[Long]
    val orderAddress = request.body.asJson.get("orderAddress").as[String]
    val productQuantity = request.body.asJson.get("productQuantity").as[Int]
    val productID = request.body.asJson.get("productID").as[Long]
    val orderDetailsPriceNet = request.body.asJson.get("orderDetailsPriceNet").as[Double]
    val orderDetailsPriceGross = request.body.asJson.get("orderDetailsPriceGross").as[Double]
    val dateFormat = new SimpleDateFormat("YYYY-MM-dd")

    orderDAO.create(userID, orderAddress, dateFormat.format(Calendar.getInstance().getTime)).map { order =>
      orderDetailDAO.create(order.orderID, productQuantity, productID, orderDetailsPriceNet, orderDetailsPriceGross)
    }.map {
      _ => Ok(Json.toJson("order_created"))
    }
  }

}

case class CreateOrderForm(userID: Long, orderAddress: String, productID: Long, productQuantity: Int, orderDetailPriceNet: Double, orderDetailPriceGross: Double)
