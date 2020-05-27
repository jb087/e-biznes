package api.basket

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.basket.OrderedProduct
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.basket.OrderedProductRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderedProductsResource @Inject()(orderedProductRepository: OrderedProductRepository,
                                        cc: MessagesControllerComponents,
                                        silhouette: Silhouette[DefaultEnv])
                                       (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOrderedProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderedProductRepository.getOrderedProducts
      .map(orderedProducts => Ok(Json.toJson(orderedProducts)))
  }

  def getOrderedProductById(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderedProductRepository.getOrderedProductByIdOption(orderedProductId)
      .map({
        case Some(orderedProduct) => Ok(Json.toJson(orderedProduct))
        case None => NotFound
      })
  }

  def createOrderedProduct: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[OrderedProduct] match {
      case JsSuccess(orderedProduct, _) => orderedProductRepository.createOrderedProduct(orderedProduct).map(_ => Ok("Ordered Product Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateOrderedProduct(orderedProductId: String): Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[OrderedProduct] match {
      case JsSuccess(orderedProduct, _) =>
        orderedProductRepository.getOrderedProductByIdOption(orderedProductId)
          .map({
            case Some(value) =>
              val orderedProductToUpdate = OrderedProduct(orderedProductId, orderedProduct.basketId, orderedProduct.productId, orderedProduct.quantity)
              Await.result(orderedProductRepository.updateOrderedProduct(orderedProductToUpdate)
                .map(_ => Ok("Ordered Product Updated!")), Duration.Inf)
            case None => InternalServerError("Ordered Product with id: " + orderedProductId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def deleteOrderedProduct(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def delete = {
      orderedProductRepository.getOrderedProductByIdOption(orderedProductId)
        .map({
          case Some(value) =>
            Await.result(orderedProductRepository.deleteOrderedProduct(orderedProductId)
              .map(_ => Ok("Removed ordered product with id: " + orderedProductId)), Duration.Inf)
          case None => InternalServerError("Ordered Product with id: " + orderedProductId + " does not exist!")
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