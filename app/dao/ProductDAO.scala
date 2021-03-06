package dao

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductDAO @Inject() (dbConfigProvider: DatabaseConfigProvider, val categoryDAO: CategoryDAO)
                           (implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  class ProductTable(tag: Tag) extends Table[Product](tag, "products") {
    def productID = column[Long]("productID", O.PrimaryKey, O.AutoInc)
    def productName = column[String]("productName")
    def productDescription = column[String]("productDescription")
    def productPriceNet = column[Double]("productPriceNet")
    def productPriceGross = column[Double]("productPriceGross")
    def categoryID = column[Long]("categoryID")

    def * = (productID, productName, productDescription, categoryID, productPriceNet, productPriceGross) <> ((Product.apply _).tupled, Product.unapply)
  }

  private val product = TableQuery[ProductTable]

  def all(): Future[Seq[Product]] = db.run {
    product.result
  }

  def getById(id: Long): Future[Seq[Product]] = db.run {
    product.filter(_.productID === id).result
  }

  def getByCategory(id: Long): Future[Seq[Product]] = db.run {
    product.filter(_.categoryID === id).result
  }

  def getByName(name: String): Future[Seq[Product]] = db.run {
    product.filter(_.productName === name).result
  }

  def create(name: String, description: String, category: Long, priceNet: Double, priceGross: Double): Future[Product] = db.run {
    (product.map(p => (p.productName, p.productDescription, p.categoryID, p.productPriceNet, p.productPriceGross))
      returning product.map(_.productID)
      into {case
      ((name, description, category, priceNet, priceGross), id) => Product(id, name, description, category, priceNet, priceGross)
    }) += (name, description, category, priceNet, priceGross)
  }

  def delete(productID: Long): Future[Int] = db.run  {
    product.filter(_.productID === productID).delete
  }

  def update(productID: Long, productName: String, productDescription: String, productPriceNet: Double,
             productPriceGross: Double, categoryID: Long): Future[Int] = db.run {
    product.filter(_.productID === productID)
      .map(prod => (prod.productName, prod.productDescription, prod.productPriceNet, prod.productPriceGross, prod.categoryID))
      .update((productName, productDescription, productPriceNet, productPriceGross, categoryID))
  }

}
