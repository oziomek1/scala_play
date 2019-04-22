package models

import play.api.libs.json._


case class Order(orderID: Long, userID: Long, orderAddress: String, orderDate: String, orderShipped: Boolean)

object Order {
  implicit val orderFormat = Json.format[Order]
}
