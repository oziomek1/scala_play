package dao

import javax.inject.{Inject, Singleton}
import models._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDAO @Inject()(/* db config */)(implicit ec: ExecutionContext){

  /*
   * place for defining the table
   *
   * waiting for Slick
   */

  def all(): Future[Seq[User]] = null

  def getById(id: Long): Future[Option[User]] = null


}
