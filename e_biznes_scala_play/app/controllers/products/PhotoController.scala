package controllers.products

import javax.inject.{Inject, Singleton}
import models.products.Photo
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.Files
import play.api.mvc._
import repositories.products.{PhotoRepository, ProductRepository}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PhotoController @Inject()(photoRepository: PhotoRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val updatePhotoForm: Form[UpdatePhotoForm] = Form {
    mapping(
      "photoId" -> nonEmptyText,
      "productId" -> nonEmptyText
    )(UpdatePhotoForm.apply)(UpdatePhotoForm.unapply)
  }

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

  def createPhotoHandler: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { request  =>
    request.body
      .file("picture")
      .map { picture =>
        val productId: String = request.body.dataParts("productId").head
        val bytes = java.nio.file.Files.readAllBytes(picture.ref.path)
        photoRepository.createPhoto(Photo("", productId, bytes))
        Redirect(routes.PhotoController.createPhoto()).flashing("success" -> "Photo created!")
      }.getOrElse {
        Redirect(routes.PhotoController.getPhotos())
    }
  }

  def updatePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = Await.result(productRepository.getProducts, Duration.Inf)

    photoRepository.getPhotoById(photoId)
      .map(photo => {
        val photoForm = updatePhotoForm.fill(UpdatePhotoForm(photo.id, photo.productId))

        Ok(views.html.photos.photoupdate(photoForm, products))
      })
  }

  def updatePhotoHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatePhotoForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val products = Await.result(productRepository.getProducts, Duration.Inf)

          BadRequest(views.html.photos.photoupdate(errorForm, products))
        }
      },
      photo => {
        photoRepository.updatePhoto(photo.photoId, photo.productId)
          .map(_ => Redirect(routes.PhotoController.updatePhoto(photo.photoId)).flashing("success" -> "Photo updated!"))
      }
    )
  }

  def deletePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    photoRepository.deletePhoto(photoId)
      .map(_ => Redirect(routes.PhotoController.getPhotos()))
  }
}

case class UpdatePhotoForm(
                            photoId: String,
                            productId: String
                          )