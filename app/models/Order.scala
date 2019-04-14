package models

import play.api.libs.json._


case class Order(id: Long, userId: Long, address: String, date: String,
                 city: String, country: String, tax: Float, shipped: Boolean)

object Order {
  implicit val orderFormat = Json.format[Order]
}
