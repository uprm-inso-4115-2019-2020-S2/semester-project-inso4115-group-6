package DAO

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException}

case class UsersDAO() {
  //setup connection to DB
  val dbName = "user"
  val hostname = "com.mysql.jdbc.Driver"

  val url ="jdbc:mysql://localhost:3306/cashforchoresdb?autoReconnect=true&useSSL=false" //link to db or file path to db
  val usernameDB = "root" //username to connect to db
  val passwordDB = "Password" // password for user to connect to db

  var connection:Connection = null


  try {
    //connection
    Class.forName(hostname)
    this.connection = DriverManager.getConnection(url, usernameDB, passwordDB)
  }
  catch {
    case e: ClassNotFoundException => e.printStackTrace();
    case e: SQLException => e.printStackTrace();
  }

  def confirmUser(email: String,upass: String): Any ={
    //prepared statement
    var checkUser: PreparedStatement = null;
    //actual query
    val checkString = "SELECT uid FROM " +
      dbName + " WHERE uemail=? AND upass=?"
    checkUser = this.connection.prepareStatement(checkString)
    //feeding username and password to statement.
    checkUser.setString(1, email)
    checkUser.setString(2, upass)
    val resultSet: ResultSet = checkUser.executeQuery()
    while(resultSet.next()){
      var result = resultSet.getInt("uid")
      if(result.getClass.getSimpleName == "int"){
        return result
      }
      else{
        return None
      }
    }
  }


  def getUserById(uid: Int): Array[Any] ={
    //prepared statement
    var checkUser: PreparedStatement = null;
    //query
    val checkString = "SELECT * " +
      "FROM user where uid=?"
    checkUser = this.connection.prepareStatement(checkString)
    //feeding uid to statement.
    checkUser.setInt(1, uid)
    val resultSet = checkUser.executeQuery()
    var result: Array[Any] = new Array[Any](5)
    while(resultSet.next()) {
      result(0) = resultSet.getInt("uid")
      result(1) = resultSet.getString(("username"))
      result(2) = resultSet.getString("email")
      result(3) = resultSet.getInt("isCustomer")
      return result
    }
    return result
  }
}