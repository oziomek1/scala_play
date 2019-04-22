package models

import play.api.libs.json._


case class OrderDetail(orderDetailID: Long, orderID: Long, productQuantity: Int, productID: Long,
                       orderDetailPriceNet: Double, orderDetailPriceGross: Double)

object OrderDetail {
  implicit val orderFormat = Json.format[OrderDetail]
}
