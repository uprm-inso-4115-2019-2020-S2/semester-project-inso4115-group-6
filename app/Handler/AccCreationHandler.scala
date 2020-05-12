package Handler

import DAO.UsersDAO
import controllers.UserData

/**
 *
 * NOTE: Uses DAO.UsersDAO to create account to discard redundancy between classes and their functions
 *
 */

class AccCreationHandler {

  def createAccount(user: UserData, provider: Boolean): String = {
      val fname = user.firstname
      val lname = user.lastname
      val phone = user.phone
      val upass = user.password
      val email = user.email
      if (!fname.isEmpty && !lname.isEmpty && !phone.isEmpty && !upass.isEmpty && !email.isEmpty) {
        val dao = UsersDAO() // to be implemented
        val result = dao.createUser(fname, lname, phone, upass, email, provider) // will return userID
        if (result.getClass.getSimpleName == "Integer") {
          return "Account Creation Successful"
        }
        else{
          return "Something went Wrong."
        }
    }
    return "Something went wrong, try again."
  }


}
