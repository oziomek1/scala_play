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

    def * = (orderID, userID, orderAddress, orderDate, orderShipped) <> ((Order.apply _).tupled, Order.unapply)
  }

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
      ((userID, address, date, shipped), id) =>
      Order(id, userID, address, date, shipped)
    }) += (userID, address, date, shipped)
  }

  def delete(orderID: Long): Future[Int] = db.run  {
    order.filter(_.orderID === orderID).delete
  }

  def update(orderID: Long, userID: Long, orderAddress: String, orderDate: String, orderShipped: Boolean) = db.run {
    order.filter(_.orderID == orderID)
      .map(ord => (ord.userID, ord.orderAddress, ord.orderDate, ord.orderShipped))
      .update((userID, orderAddress, orderDate, orderShipped))
  }

  def getByUserId(userId: Long): Future[Seq[Order]] = db.run {
    order.filter(_.userID === userId).result
  }

}
