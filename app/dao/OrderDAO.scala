package dao

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDAO @Inject()(dbConfigProvider: DatabaseConfigProvider, val userDAO: UserDAO)
                        (implicit ec: ExecutionContext){

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "orders") {
    def orderID = column[Long]("orderID", O.PrimaryKey, O.AutoInc)
    def orderAddress = column[String]("orderAddress")
    def orderDate = column[String]("orderDate")
    def orderShipped = column[Boolean]("orderShipped")
    def userID = column[Long]("userID")
    def user_fk = foreignKey("user_fk", userID, user)(_.userID)

    def * = (orderID, userID, orderAddress, orderDate, orderShipped) <> ((Order.apply _).tupled, Order.unapply)
  }

  import userDAO.UserTable
  private val user = TableQuery[UserTable]
  private val order = TableQuery[OrderTable]

  def all(): Future[Seq[Order]] = db.run {
    order.result
  }

  def getById(id: Long): Future[Seq[Order]] = db.run {
    order.filter(_.orderID === id).result
  }

  def create(userID: Long, address: String, date: String, shipped: Boolean = false): Future[Order] = db.run {
    (order.map(o => (o.userID, o.orderAddress, o.orderDate, o.orderShipped))
      returning order.map(_.orderID)
      into {case
      ((userID, address, date, shipped), id) => Order(id, userID, address, date, shipped)
    }) += (userID, address, date, shipped)
  }

  def getByUserId(userId: Long): Future[Seq[Order]] = db.run {
    order.filter(_.userID === userId).result
  }

}
