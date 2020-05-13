package Handler

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

class ProfileHandler {

	def buildFeedbackDic(row: Array[Any]): Unit = {
		var result: Map[String, Any] = Map()
		result += ("fid" -> row(0))
		result += ("Description" -> row(1))
		result += ("cuid" -> row(1))
		result += ("spid" -> row(2))
		return result
	}

	def giveFeedback(form: Map[String, Any]): Unit = {
		if(form.size == 3){
			val cuid = form.get("cuid").asInstanceOf[Int]
			val spid = form.get("spid").asInstanceOf[Int]
			val feedback = form.get("description").asInstanceOf[String]
			if(!feedback.isEmpty && (cuid.getClass.getSimpleName == "Integer") && (spid.getClass.getSimpleName == "Integer")){
				val dao = ProfileDAO()
				val result = dao.giveFeedback(cuid, spid, feedback)
				if(result.getClass.getSimpleName == "int"){
					return "Successfully gave feedback"
				}
			}
		}
		return "Feedback has an error"
	}

	def getFeedback(form: Map[String, Any]): Unit = {
		if(form.size == 2){
			val cuid = form.get("cuid").asInstanceOf[Int]
			val spid = form.get("spid").asInstanceOf[Int]
			if((cuid.getClass.getSimpleName == "Integer") && (spid.getClass.getSimpleName == "Integer")){
				val dao = ProfileDAO()
				val result = dao.getFeedback(cuid, spid)
				if(result == null){ //do something

				}
				val resultDic = buildFeedbackDic(result.asInstanceOf[Array[Any]])
				//For loop thru result and create a json representation of the data.
				return "Successfully got feedback"
			}
		}
		return "Feedback has an error"
	}

	def updateServiceDescription(form: Map[String, Any]): Unit = {
		if(form.size == 2){
			val uid = form.get("uid").asInstanceOf[Int]
			val serviceDescription = form.get("serviceDesciption").asInstanceOf[String]
			if(!serviceDescription.isEmpty && (uid.getClass.getSimpleName == "Integer")){
				val dao = ProfileDAO()
				val result = dao.updateServiceDescription(uid, serviceDescription)
				if(result.getClass.getSimpleName == "Integer"){
					return "Successfully updated the service description."
				}
			}
		}
		return "There was an error updating the service description"
	}

	def getServiceDescription(form: Map[String, Any]): Unit = {
		if(form.size == 1){
			val uid = form.get("uid").asInstanceOf[Int]
			if((uid.getClass.getSimpleName == "Integer")){
				val dao = ProfileDAO()
				val result = dao.getServiceDescription(uid)
				if(result != null &&  !result.toString.isEmpty){
					//it was successfully returned as a string.
					return result
				}
			}
		}
		return ""  // Blank return.
	}

}

case class ProfileDAO(){
	val dbName = "cashforchoresdb"
	val hostName = "localhost"
	val url = ""
	val usernameDB = "root"
	val passwordDB = "Password"
	var connection: Connection = null

	try{
		Class.forName(hostName)
		this.connection = DriverManager.getConnection(url, usernameDB, passwordDB)
	}
	catch{
		case e: Any => e.printStackTrace()
	}

	def giveFeedback(cuid: Int, spid: Int, feedback: String){
		var conn: PreparedStatement = null
		val query = "INSERT INTO feedback (Description, cuid, spid) VALUES (?, ?, ?); SELECT LAST_INSERT_ID();"
		conn = this.connection.prepareStatement(query)
		conn.setString(1, feedback)
		conn.setInt(2, cuid)
		conn.setInt(3, spid)
		val resultSet: ResultSet = conn.executeQuery()
		val result = resultSet.getInt("fid")
		if(result > 0) this.connection.commit()
		return result
	}

	def getFeedback(cuid: Int, spid: Int): Unit ={
		var conn: PreparedStatement = null
		val query = "SELECT fid, Description, cuid, spid FROM feedback WHERE cuid = ? and spid = ?;"
		conn = this.connection.prepareStatement(query)
		conn.setInt(1, cuid)
		conn.setInt(2, spid)
		val resultSet: ResultSet = conn.executeQuery()
		if(resultSet.getFetchSize == 0) return null
		val result = resultSet.getString("Description")
		return result
	}

	def updateServiceDescription(uid: Int, description: String){
		var conn: PreparedStatement = null
		val query = "SET @update_id := 0; UPDATE cashforchoresdb.serviceprovider SET serviceDescription = ?, spid = (SELECT @update_id := spid) WHERE uid = ?; SELECT @update_id;"
		conn = this.connection.prepareStatement(query)
		conn.setString(1, description)
		conn.setInt(2, uid)
		val resultSet: ResultSet = conn.executeQuery()
		val result = resultSet.getInt("@update_id")
		if (result > 0) this.connection.commit()
		return result
	}

	def getServiceDescription(uid: Int): Unit ={
		var conn: PreparedStatement = null
		val query = "SELECT serviceDescription FROM cashforchoresdb.serviceprovider WHERE uid = ?;"
		conn = this.connection.prepareStatement(query)
		conn.setInt(1, uid)
		val resultSet: ResultSet = conn.executeQuery()
		val result = resultSet.getString("serviceDescription")
		return result
	}
}

