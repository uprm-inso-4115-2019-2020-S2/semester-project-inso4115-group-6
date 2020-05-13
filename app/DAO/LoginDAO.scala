package DAO

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException}

case class UsersDAO() {
  //setup connection to DB
  val dbName = "user"
  val driver = "com.mysql.jdbc.Driver"

  val url ="jdbc:mysql://localhost:3306/cashforchoresdb?autoReconnect=true&useSSL=false" //link to db or file path to db
  val usernameDB = "root" //username to connect to db
  val passwordDB = "Password" // password for user to connect to db

  var connection:Connection = null

  try {
    //connection
    Class.forName(driver)
    this.connection = DriverManager.getConnection(url, usernameDB, passwordDB)
  }
  catch {
    case e: ClassNotFoundException => e.printStackTrace();
    case e: SQLException => e.printStackTrace();
  }
  // TODO: Location & Work Types for Service Provider
  def createUser(fname: String, lname: String, phone: String, upass: String, email: String, provider: Boolean, job: String): Any = {
    // Declare Prepared Statements
    var stmtInsertUser: PreparedStatement = null
    var stmtGetUser: PreparedStatement = null
    var stmtInsertContactInfo: PreparedStatement = null
    var stmtInsertType: PreparedStatement = null
    // Query strings
    val insertUserQ: String = "INSERT INTO cashforchoresdb.user (uemail, isCustomer, upass) VALUES(?, ?, ?);"
    val getUserQ: String = "SELECT uid FROM cashforchoresdb.user WHERE uemail=? AND upass=?;"
    val insertContactInfoQ: String = "INSERT INTO cashforchoresdb.contactinformation (firstname, middlename, lastname, gender, phoneNumber, Address, uid) VALUES(?, ?, ?, ?, ?, ?, ?);"
    // Prepare statements
    stmtInsertUser = this.connection.prepareStatement(insertUserQ)
    stmtGetUser = this.connection.prepareStatement(getUserQ)
    stmtInsertContactInfo = this.connection.prepareStatement(insertContactInfoQ)

    // Set Values for User Insert on User Table
    stmtInsertUser.setString(1, email)
    stmtInsertUser.setInt(2, if(provider) 1 else 0)
    stmtInsertUser.setString(3, upass)
    // Set Values to get User ID
    stmtGetUser.setString(1, email)
    stmtGetUser.setString(2, upass)

    // Execute Queries as a Transaction
    try {
      this.connection.setAutoCommit(false)
      // Insert User
      stmtInsertUser.executeUpdate()
      // Get User ID
      var rs: ResultSet = stmtGetUser.executeQuery()
      rs.next()
      val userId = rs.getInt("uid")

      // Set Values to Insert Contact Information
      stmtInsertContactInfo.setString(1, fname)
      stmtInsertContactInfo.setString(2, "")
      stmtInsertContactInfo.setString(3, lname)
      stmtInsertContactInfo.setString(4, "")
      stmtInsertContactInfo.setString(5, phone)
      stmtInsertContactInfo.setString(6, "")
      stmtInsertContactInfo.setInt(7, userId)
      // Insert Contact Info
      stmtInsertContactInfo.executeUpdate()

      // Prepare a Final statement to Identify user Type
      var userTypeQuery = ""
      if(provider) {
        userTypeQuery = "INSERT INTO cashforchoresdb.serviceprovider (uid,serviceDescription,servicetype) VALUES(?,?,?);"
        // Prepare Statement
        stmtInsertType = this.connection.prepareStatement(userTypeQuery)
        stmtInsertType.setInt(1,userId)
        stmtInsertType.setString(2,"")
        stmtInsertType.setString(3,job)
      } else {
        userTypeQuery = "INSERT INTO cashforchoresdb.customer (uid) VALUES(?);"
        // Prepare Statement
        stmtInsertType = this.connection.prepareStatement(userTypeQuery)
        stmtInsertType.setInt(1,userId)
      }
      // Insert User Type
      stmtInsertType.executeUpdate()

      this.connection.commit()
    } catch {
      case error: SQLException => {
        this.connection.rollback()
        error.printStackTrace()
        // TODO: Let user know that something went wrong
      }
    } finally {
      this.connection.setAutoCommit(true)
      this.connection.close()
    }
  }


  def confirmUser(email: String, upass: String): Any ={
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