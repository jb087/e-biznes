package api.products

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.categories.SubcategoryRepository
import repositories.products.ProductRepository

import scala.concurrent.ExecutionContext

@Singleton
class ProductResource @Inject()(productRepository: ProductRepository, subcategoryRepository: SubcategoryRepository,
                                cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getProductById(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateProduct(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteProduct(productId: String): Action[AnyContent] = Action {

  }
}