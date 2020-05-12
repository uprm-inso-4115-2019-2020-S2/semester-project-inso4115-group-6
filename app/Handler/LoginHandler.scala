import scala.collection.mutable.Map

case class UsersHandler() {
  def buildUsersDict(row: Array[Any]): Map[String,Any] = {
    var result: Map[String, Any] = Map()
    result += ("uid" -> row(0))
    result += (("username") -> row(1))
    result += ("password" -> row(2))
    result += ("firstName" -> row(3))
    result += ("lastName" -> row(4))
    result += ("email" -> row(5))
    return result
  }

  def login(form: Map[String, Any]): String = {
    if (form.size >= 2) {
      val uname = form("username").toString
      val upass = form("password").toString
      if (!uname.isEmpty && !upass.isEmpty) {
        val dao = UsersDAO() // to be implemented
        val result = dao.confirmUser(uname, upass) // will return userID
        if (result.getClass.getSimpleName == "Integer") {
          return "Log-in Successful"
        }
        else{
          return "Username or password is incorrect."
        }
      }
    }
    return "Log-in unsuccessul, try again."
  }

}