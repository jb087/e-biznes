package api.products

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.categories.SubcategoryRepository
import repositories.products.ProductRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductResource @Inject()(productRepository: ProductRepository, subcategoryRepository: SubcategoryRepository,
                                cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def getProductById(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def createProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def updateProduct(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def deleteProduct(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }
}