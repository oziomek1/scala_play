package models

import play.api.libs.json._


case class OrderDetail(id: Long, productID: Long, quantity: Int, price: Float)

object OrderDetail {
  implicit val orderFormat = Json.format[Order]
}
