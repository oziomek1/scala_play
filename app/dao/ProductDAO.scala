package dao

import javax.inject.{Inject, Singleton}
import models._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductDAO @Inject()(/* db config */)(implicit ec: ExecutionContext) {

  /*
   * place for defining the table
   *
   * waiting for Slick
   */

  def all(): Future[Seq[Product]] = null

  def getById(id: Long): Future[Option[Product]] = null

  def getByCategoryId(categoryId: Long): Future[Seq[Product]] = null

  def create(name: String, description: String, category: Long, price: Float): Future[Product] = null

  def update(id: Long, name: String, description: String, category: Long, price: Float): Future[Product] = null

  def delete(id: Long): Future[Unit] = null

}
