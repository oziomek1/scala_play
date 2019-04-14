package models

import java.text.SimpleDateFormat

import play.api.libs.json._


case class Order(id: Long, userId: Long, address: String, date: SimpleDateFormat,
                 city: String, country: String, tax: Float, shipped: Boolean)

object Order {
  implicit val orderFormat = Json.format[Order]
}
