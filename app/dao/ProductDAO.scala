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

  def create(product: Product): Future[Unit] = null

  def update(id: Long, product: Product): Future[Unit] = null

  def delete(id: Long): Future[Unit] = null

}
