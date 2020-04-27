package api.products

import javax.inject.{Inject, Singleton}
import models.products.Photo
import play.api.libs.Files
import play.api.libs.json.Json
import play.api.mvc._
import repositories.products.PhotoRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

@Singleton
class PhotoResource @Inject()(photoRepository: PhotoRepository, cc: MessagesControllerComponents)
                             (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getPhotos: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotos
      .map(photos => Ok(Json.toJson(photos)))
  }

  def getPhotoById(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotoByIdOption(photoId)
      .map({
        case Some(photo) => Ok(Json.toJson(photo))
        case None => NotFound
      })
  }

  def createPhoto: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { request =>
    request.body
      .file("picture")
      .map { picture =>
        val productId: String = request.body.dataParts("productId").head
        val bytes = java.nio.file.Files.readAllBytes(picture.ref.path)
        Await.result(photoRepository.createPhoto(Photo("", productId, bytes)), Duration.Inf)
        Ok("Photo Created!")
      }.getOrElse {
      InternalServerError("Provided multipart data form is not valid. Please provide correct multipart data form.")
    }
  }

  def updatePhoto(photoId: String, productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.updatePhoto(photoId, productId)
      .map(_ => Ok("Photo Updated!"))
  }

  def deletePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotoByIdOption(photoId)
      .map({
        case Some(value) =>
          Await.result(photoRepository.deletePhoto(photoId)
            .map(_ => Ok("Removed photo with id: " + photoId)), Duration.Inf)
        case None => InternalServerError("Photo with id: " + photoId + " does not exist!")
      })
  }
}