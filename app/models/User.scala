package models

import play.api.libs.json._


case class User(userID: Long, userEmail: String, userPassword: String,
                userFirstname: String, userLastname: String, userAddress: String)

object User {
  implicit val userFormat = Json.format[User]
}
