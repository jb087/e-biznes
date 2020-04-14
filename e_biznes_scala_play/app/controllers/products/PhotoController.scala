package controllers.products

import java.nio.file.Files

import javax.inject.{Inject, Singleton}
import models.products.{Photo, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.products.{PhotoRepository, ProductRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

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

  def updatePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var products: Seq[Product] = Seq[Product]()
    productRepository.getProducts.onComplete {
      case Success(productsFromFuture) => products = productsFromFuture
      case Failure(_) => print("Failed products download")
    }

    photoRepository.getPhotoById(photoId)
      .map(photo => {
        val photoForm = updatePhotoForm.fill(UpdatePhotoForm(photo.id, photo.productId))

        Ok(views.html.photos.photoupdate(photoForm, products))
      })
  }

  def updatePhotoHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatePhotoForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          var products: Seq[Product] = Seq[Product]()
          productRepository.getProducts.onComplete {
            case Success(productsFromFuture) => products = productsFromFuture
            case Failure(_) => print("Failed products download")
          }

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