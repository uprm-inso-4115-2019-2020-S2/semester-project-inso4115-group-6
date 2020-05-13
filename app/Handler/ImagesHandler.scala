package Handler

import java.io.FileInputStream
import java.util.Base64

import DAO.ImagesDAO

case class ImagesHandler() {

  def buildImagesDict(row: Array[Any]): Map[String,Any] = {
    var result: Map[String, Any] = Map()

    // Attribute names are just placeholders for now
    // pic_id
    // pic_string
    // user_id
    // is_profilepic boolean

    result += ("pic_id" -> row(0))
    result += (("pic_string") -> row(1))
    result += ("user_id" -> row(2))
    result += ("is_profilepic" -> row(3))
    return result

  }

  def uploadImage(imgPath: String): Unit = {

    // Not sure how to implement receiving the image from the controller
    // But the idea is that this function receives the image path and
    // calls the function to encode it, then invokes the DAO

    val imageString = encodeImage(imgPath)
    val dao = ImagesDAO()
    val result = dao.uploadImage(imageString, null, null)

  }

  def deleteImage(img_id: Int): Unit = {

    val dao = ImagesDAO()
    dao.deleteImage(img_id)

  }


  def encodeImage(imgPath: String): String = {

    val imageStream = new FileInputStream(imgPath)
    // get byte array from image stream
    val byteArr = imageStream.readAllBytes
    // encode the image
    val imageString = Base64.getEncoder.encodeToString(byteArr)

    imageString
  }


  // Not sure if needed
  def decodeImage(imgString: String): Unit = {
    val data: Array[Byte] = Base64.getDecoder.decode(imgString)

  }

}
