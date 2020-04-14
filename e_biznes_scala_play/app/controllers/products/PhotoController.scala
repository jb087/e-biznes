package controllers.products

import java.nio.file.Files

import javax.inject.{Inject, Singleton}
import models.products.Photo
import play.api.mvc._
import repositories.products.{PhotoRepository, ProductRepository}

import scala.concurrent.ExecutionContext

@Singleton
class PhotoController @Inject()(photoRepository: PhotoRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getPhotos: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotos
      .map(photos => Ok(views.html.photos.photos(photos)))
  }

  def getPhotoById(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotoByIdOption(photoId)
      .map({
        case Some(photo) => Ok(views.html.photos.photo(photo))
        case None => Redirect(routes.PhotoController.getPhotos())
      })
  }

  def getPhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotoByIdOption(photoId)
      .map({
        case Some(photo) => Ok(photo.photo).as("image/jpeg")
        case None => InternalServerError("Photo " + photoId + " not found!")
      })
  }

  def createPhoto: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProducts
        .map(products => Ok(views.html.photos.photoadd(products)))
  }

  def createPhotoHandler = Action(parse.multipartFormData) { request  =>
    request.body
      .file("picture")
      .map { picture =>
        val productId: String = request.body.dataParts("productId").head
        val bytes = Files.readAllBytes(picture.ref.path)
        photoRepository.createPhoto(Photo("", productId, bytes))
        Redirect(routes.PhotoController.createPhoto()).flashing("success" -> "Photo created!")
      }.getOrElse {
        Redirect(routes.PhotoController.getPhotos())
    }
  }

  def updatePhoto(photoId: String) = Action {
    Ok("")
  }

  def deletePhoto(photoId: String) = Action {
    Ok("")
  }
}
