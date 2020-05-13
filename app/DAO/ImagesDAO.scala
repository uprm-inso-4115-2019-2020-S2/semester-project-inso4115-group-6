package DAO

import java.io.FileInputStream
import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException}

case class ImagesDAO() {

  val hostname = "com.mysql.jdbc.Driver"
  val url ="jdbc:mysql://localhost:3306/cashforchoresdb" // not sure if correct
  val username = "root"
  val password = "Password"

  var connection:Connection = null

  try {
    Class.forName(hostname)
    this.connection = DriverManager.getConnection(url, username, password)
  }

  catch {
    case e: Any => e.printStackTrace()
  }


  
  def uploadImage(imgString: FileInputStream, user_id: Int, is_profilepic: Boolean): Unit = {
    this.connection.setAutoCommit(false)
    var conn: PreparedStatement = null

    // may need to change names to match those in db
    val query1 = "INSERT INTO cashforchoresdb.pictures (pBlob, uid, isProfilePicture) VALUES (?, ?, ?);"
    val query2 = "SELECT LAST_INSERT_ID()"
    conn = this.connection.prepareStatement(query1)
    conn.setBlob(1, imgString)
    conn.setInt(2, user_id)
    conn.setBoolean(3, is_profilepic)

    val resultSet: Int = conn.executeUpdate()

    if(resultSet > 0)
      this.connection.commit()

    resultSet

  }

  def deleteImage(img_id: Int): Unit = {

    var conn: PreparedStatement = null

    // may need to change names to match those in db
    val query = "DELETE FROM images where img_id=?;"
    conn = this.connection.prepareStatement(query)
    conn.setInt(1, img_id)

    conn.executeQuery()

    // not sure if it would work as it is
  }




}
