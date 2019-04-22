package dao

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDetailDAO @Inject()(dbConfigProvider: DatabaseConfigProvider, val orderDAO: OrderDAO, val productDAO: ProductDAO)
                              (implicit ec: ExecutionContext){

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private class OrderDetailTable(tag: Tag) extends Table[OrderDetail](tag, "orderDetails") {
    def orderDetailID = column[Long]("orderDetailID", O.PrimaryKey, O.AutoInc)
    def orderID = column[Long]("orderID")
    def productQuantity = column[Int]("productQuantity")
    def productID = column[Long]("productID")
    def orderDetailPriceNet = column[Double]("orderDetailPriceNet")
    def orderDetailPriceGross = column[Double]("orderDetailPriceGross")
    def order_fk = foreignKey("order_fk", orderID, order)(_.orderID)
    def product_fk = foreignKey("product_fk", productID, product)(_.productID)

    def * = (orderDetailID, orderID, productQuantity, productID, orderDetailPriceNet, orderDetailPriceGross) <> ((OrderDetail.apply _).tupled, OrderDetail.unapply)
  }

  private val order = TableQuery[orderDAO.OrderTable]
  private val product = TableQuery[productDAO.ProductTable]
  private val orderDetail = TableQuery[OrderDetailTable]


  def getById(id: Long): Future[Seq[OrderDetail]] = db.run {
    orderDetail.filter(_.orderDetailID === id).result
  }

  def getByOrderId(id: Long): Future[Seq[OrderDetail]] = db.run {
    orderDetail.filter(_.orderID === id).result
  }

}
