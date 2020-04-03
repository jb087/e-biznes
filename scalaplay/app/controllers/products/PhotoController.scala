package controllers.products

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class PhotoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getPhotos = Action {
    Ok("")
  }

  def createPhoto = Action {
    Ok("")
  }

  def updatePhoto(photoId: String) = Action {
    Ok("")
  }

  def deletePhoto(photoId: String) = Action {
    Ok("")
  }
}
