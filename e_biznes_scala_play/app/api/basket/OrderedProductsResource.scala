package api.basket

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.basket.{BasketRepository, OrderedProductRepository}
import repositories.products.ProductRepository

import scala.concurrent.ExecutionContext

@Singleton
class OrderedProductsResource @Inject()(orderedProductRepository: OrderedProductRepository, basketRepository: BasketRepository,
                                        productRepository: ProductRepository, cc: MessagesControllerComponents)
                                       (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOrderedProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getOrderedProductById(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createOrderedProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateOrderedProduct(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteOrderedProduct(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>


  }
}