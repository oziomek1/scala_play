package dao

import javax.inject.{Inject, Singleton}
import models._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDAO @Inject()(/* db config */)(implicit ec: ExecutionContext){

  /*
   * place for defining the table
   *
   * waiting for Slick
   */

  def all(): Future[Seq[Order]] = null

  def getById(id: Long): Future[Option[Order]] = null

  def create(order: Order): Future[Order] = null

  def update(id: Long, order: Order): Future[Order] = null

  def getByUserId(userId: Long): Future[Option[Order]] = null

}
