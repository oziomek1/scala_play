package dao

import javax.inject.{Inject, Singleton}
import models._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryDAO @Inject()(/* db config */)(implicit ec: ExecutionContext){

  /*
   * place for defining the table
   *
   * waiting for Slick
   */

  def all(): Future[Seq[Category]] = null

  def getById(id: Long): Future[Option[Category]] = null

}
