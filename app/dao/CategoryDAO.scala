package dao

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryDAO @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "categories") {
    def categoryID = column[Long]("categoryID", O.PrimaryKey, O.AutoInc)
    def categoryName = column[String]("categoryName")

    def * = (categoryID, categoryName) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]

  def all(): Future[Seq[Category]] = db.run {
    category.result
  }

  def getById(id: Long): Future[Seq[Category]] = db.run {
    category.filter(_.categoryID === id).result
  }

  def getByName(name: String): Future[Seq[Category]] = db.run {
    category.filter(_.categoryName === name).result
  }

  def create(name: String): Future[Category] = db.run {
    (category.map(c => (c.categoryName))
      returning category.map(_.categoryID)
      into {case
      ((name), id) => Category(id, name)
    }) += (name)
  }

  def delete(categoryID: Long): Future[Int] = db.run  {
    category.filter(_.categoryID === categoryID).delete
  }

  def update(categoryID: Long, categoryName: String): Future[Int] = db.run {
    category.filter(_.categoryID === categoryID).map(cat => cat.categoryName).update(categoryName)
  }

}
