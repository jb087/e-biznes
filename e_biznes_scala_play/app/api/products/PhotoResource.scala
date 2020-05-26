package api.products

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.products.Photo
import play.api.libs.Files
import play.api.libs.json.Json
import play.api.mvc._
import repositories.products.PhotoRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PhotoResource @Inject()(photoRepository: PhotoRepository,
                              cc: MessagesControllerComponents,
                              silhouette: Silhouette[DefaultEnv])
                             (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getPhotos: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotos
      .map(photos => Ok(Json.toJson(photos)))
  }

  def getPhotosByProductId(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotoByProductId(productId)
      .map(photos => Ok(Json.toJson(photos)))
  }

  def getPhotoById(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.getPhotoByIdOption(photoId)
      .map({
        case Some(photo) => Ok(Json.toJson(photo))
        case None => NotFound
      })
  }

  def createPhoto: Action[MultipartFormData[Files.TemporaryFile]] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.multipartFormData) {
    implicit request: SecuredRequest[DefaultEnv, MultipartFormData[Files.TemporaryFile]] =>
      request.body
        .file("picture")
        .map { picture =>
          val productId: String = request.body.dataParts("productId").head
          val bytes = java.nio.file.Files.readAllBytes(picture.ref.path)
          photoRepository.createPhoto(Photo("", productId, bytes))
            .map(_ => Ok("Photo Created!"))
        }.getOrElse {
        Future.successful(InternalServerError("Provided multipart data form is not valid. Please provide correct multipart data form."))
      }
  }

  def updatePhoto(photoId: String, productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.updatePhoto(photoId, productId)
      .map(_ => Ok("Photo Updated!"))
  }

  def deletePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def delete = {
      photoRepository.getPhotoByIdOption(photoId)
        .map({
          case Some(value) =>
            Await.result(photoRepository.deletePhoto(photoId)
              .map(_ => Ok("Removed photo with id: " + photoId)), Duration.Inf)
          case None => InternalServerError("Photo with id: " + photoId + " does not exist!")
        })
    }

    silhouette.SecuredRequestHandler(HasRole(UserRoles.Admin)) { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Await.result(delete, Duration.Inf)
      case HandlerResult(r, None) => Unauthorized
    }
  }
}