//package services
//
//import javax.inject.Inject
//
//import com.mohiva.play.silhouette.api.LoginInfo
//import dao.UserDAO
//import models.User
//
//import play.api.db.slick.DatabaseConfigProvider
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class UserServiceImpl @Inject() (
//    protected val dbConfigProvider: DatabaseConfigProvider)(
//    implicit ex: ExecutionContext)
//    extends UserService {
//
//  import profile.api._
//
//  override def retrieve(id: Long): Future[Option[User]] = {
//    val userQuery = for {
//      dbLoginInfo <- loginInfoQuery(loginInfo)
//      dbUserLoginInfo <- userLoginInfos.filter(_.loginInfoId === dbLoginInfo.userID)
//      dbUser <- users.filter.(_.userID === dbUserLoginInfo.userID)
//    } yield dbUser
//    db.run(userQuery.result.headOption).map { dbUserOption =>
//      dbUserOption.map { user =>
//        User(
//          user.userID,
//          user.loginInfo,
//          user.userEmail,
//          user.userFirstname,
//          user.userLastname,
//          user.userAddress,
//          user.userPassword)
//      }
//    }
//  }
//
//}
