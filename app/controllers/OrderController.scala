package controllers

import dao._
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class OrderController @Inject()(orderDAO: OrderDAO,
                                userDAO: UserDAO,
                                cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "userID" -> longNumber,
      "orderAddress" -> text.verifying(minLength(6), maxLength(100)),
      "orderDate" -> text,
      "orderShipped" -> boolean,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def index = Action.async { implicit request =>
    val users = userDAO.all()
    users.map(user => Ok(views.html.indexOrder(orderForm, user)))
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

  def addOrder = Action.async { implicit request =>
    var a:Seq[User] = Seq[User]()
    val users = userDAO.all().onComplete{
      case Success(users) => a= users
      case Failure(_) => print("fail")
    }
    orderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.indexOrder(errorForm, a)))
      },
      order => {
        orderDAO.create(order.userID, order.orderAddress, order.orderDate, order.orderShipped).map {
          _ => Redirect(routes.OrderController.index).flashing("success" -> "order.created")
        }
      }
    )
  }

}

case class CreateOrderForm(userID: Long, orderAddress: String, orderDate: String, orderShipped: Boolean)
