package DAO

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


  
  def uploadImage(imgString: String, user_id: Int, is_profilepic: Boolean): Unit = {

    var conn: PreparedStatement = null

    // may need to change names to match those in db
    val query = "INSERT INTO images (img_string, user_id, is_profilepic) VALUES (?, ?, ?); SELECT LAST_INSERT_ID()"

    conn = this.connection.prepareStatement(query)
    conn.setString(1, imgString)
    conn.setInt(2, user_id)
    conn.setBoolean(3, is_profilepic)

    val resultSet: ResultSet = conn.executeQuery()
    val result = resultSet.getInt("img_id")

    if(result > 0)
      this.connection.commit()

    result

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
