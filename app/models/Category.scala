package models

import play.api.libs.json.Json


case class Category(categoryID: Long, categoryName: String)

object Category {
  implicit val categoryFormat = Json.format[Category]
}
