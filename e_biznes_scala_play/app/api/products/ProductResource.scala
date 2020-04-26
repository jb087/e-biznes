package api.products

import javax.inject.{Inject, Singleton}
import models.products.Product
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.categories.SubcategoryRepository
import repositories.products.ProductRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ProductResource @Inject()(productRepository: ProductRepository, subcategoryRepository: SubcategoryRepository,
                                cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProducts
      .map(products => Ok(Json.toJson(products)))
  }

  def getProductById(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProductByIdOption(productId)
      .map({
        case Some(product) => Ok(Json.toJson(product))
        case None => NotFound
      })
  }

  def createProduct: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Product] match {
      case JsSuccess(product, _) => productRepository.createProduct(product).map(_ => Ok("Product Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateProduct(productId: String): Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Product] match {
      case JsSuccess(product, _) =>
        productRepository.getProductByIdOption(productId)
          .map({
            case Some(value) =>
              val productToUpdate = Product(productId, product.subcategoryId, product.title, product.price, product.description, product.date, product.quantity)
              Await.result(productRepository.updateProduct(productToUpdate)
                .map(_ => Ok("Product Updated!")), Duration.Inf)
            case None => InternalServerError("Product with id: " + productId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def deleteProduct(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProductByIdOption(productId)
      .map({
        case Some(value) =>
          Await.result(productRepository.deleteProduct(productId)
            .map(_ => Ok("Removed product with id: " + productId)), Duration.Inf)
        case None => InternalServerError("Product with id: " + productId + " does not exist!")
      })
  }
}