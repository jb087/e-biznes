package api.products

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.products.{PhotoRepository, ProductRepository}

import scala.concurrent.ExecutionContext

@Singleton
class PhotoResource @Inject()(photoRepository: PhotoRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)
                             (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getPhotos: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getPhotoById(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createPhoto: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updatePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deletePhoto(photoId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }
}