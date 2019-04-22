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

  val user = TableQuery[UserTable]

  def all(): Future[Seq[User]] = db.run {
    user.result
  }

  def getById(id: Long): Future[Seq[User]] = db.run {
    user.filter(_.userID === id).result
  }

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def userID = column[Long]("userId", O.PrimaryKey, O.AutoInc)
    def userEmail = column[String]("userEmail")
    def userFirstname = column[String]("userFirstname")
    def userLastname = column[String]("userLastname")
    def userAddress = column[String]("userAddress")

    def * = (userID, userEmail, userFirstname, userLastname, userAddress) <> ((User.apply _).tupled, User.unapply)
  }
}
