package models

import play.api.libs.json._


case class Product(productID: Long, productName: String, productDescription: String, categoryID: Long,
                   productPriceNet: Double, productPriceGross: Double)

object Product {
  implicit val productFormat = Json.format[Product]
}
