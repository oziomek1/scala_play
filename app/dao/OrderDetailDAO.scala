package dao

import javax.inject.{Inject, Singleton}
import models._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDetailDAO @Inject()(/* db config */)(implicit ec: ExecutionContext){

  /*
   * place for defining the table
   *
   * waiting for Slick
   */

  def getById(id: Long): Future[Option[OrderDetail]] = null

  def getByOrderId(orderId: Long): Future[Option[OrderDetail]] = null

}
