package models

import play.api.libs.json._


case class User(id: Long, email: String, firstName: String, lastName: String, address: String)

object User {
  implicit val userFormat = Json.format[User]
}
