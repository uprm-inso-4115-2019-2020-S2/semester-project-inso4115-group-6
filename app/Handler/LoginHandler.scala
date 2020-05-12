package HandlerLogin

import scala.collection.mutable.Map
import DAO._

case class UsersHandler() {
  def buildUsersDict(row: Array[Any]): Map[String,Any] = {
    var result: Map[String, Any] = Map()
    result += ("uid" -> row(0))
    result += (("username") -> row(1)) // no se si remover
    result += ("password" -> row(2))
    result += ("firstName" -> row(3))
    result += ("lastName" -> row(4))
    result += ("email" -> row(5))
    return result
  }
  def buildLoginDict(row: Array[Any]): Map[String,Any] = {
    var result: Map[String, Any] = Map()
    result += (("email") -> row(0))
    result += ("password" -> row(1))
    return result
  }

  def login(form: Map[String, Any]): Boolean = {
    if (form.size >= 2) {
      val email = form("email").toString
      val upass = form("password").toString
      if (!email.isEmpty && !upass.isEmpty) {
        val dao = UsersDAO() // to be implemented
        val result = dao.confirmUser(email, upass) // will return userID
        if (result.getClass.getSimpleName == "Integer") {
          return true
        }
        else{
          return false
        }
      }
    }
    return false
  }

}