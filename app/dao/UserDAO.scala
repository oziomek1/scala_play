package dao

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDAO @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def userID = column[Long]("userId", O.PrimaryKey, O.AutoInc)
    def userEmail = column[String]("userEmail")
    def userPassword = column[String]("userPassword")
    def userFirstname = column[String]("userFirstname")
    def userLastname = column[String]("userLastname")
    def userAddress = column[String]("userAddress")

    def * = (userID, userPassword, userEmail, userFirstname, userLastname, userAddress) <> ((User.apply _).tupled, User.unapply)
  }

  val user = TableQuery[UserTable]

  def all(): Future[Seq[User]] = db.run {
    user.result
  }

  def getById(id: Long): Future[Seq[User]] = db.run {
    user.filter(_.userID === id).result
  }

  def create(email: String, password: String, firstname: String, lastname: String, address: String) = db.run {
    (user.map(u => (u.userEmail, u.userPassword, u.userFirstname, u.userLastname, u.userAddress))
      returning user.map(_.userID)
      into {case
      ((email, password, firstname, lastname, address), id) => User(id, password, email, firstname, lastname, address)
    }) += (email, password, firstname, lastname, address)
  }



}
