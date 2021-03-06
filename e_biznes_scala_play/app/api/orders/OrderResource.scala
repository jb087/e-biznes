package api.orders

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.orders.Order
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.orders.OrderRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderResource @Inject()(orderRepository: OrderRepository,
                              cc: MessagesControllerComponents,
                              silhouette: Silhouette[DefaultEnv])
                             (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOrders: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getOrders
      .map(orders => Ok(Json.toJson(orders)))
  }

  def getOrderById(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getOrderByIdOption(orderId)
      .map({
        case Some(order) => Ok(Json.toJson(order))
        case None => NotFound
      })
  }

  def createOrder: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Order] match {
      case JsSuccess(order, _) => orderRepository.createOrder(order).map(orderId => Ok(orderId))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateOrder(orderId: String): Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[Order] match {
      case JsSuccess(order, _) =>
        orderRepository.getOrderByIdOption(orderId)
          .map({
            case Some(value) =>
              val orderToUpdate = Order(orderId, order.basketId, order.shippingInformationId, order.state)
              Await.result(orderRepository.updateOrder(orderToUpdate)
                .map(_ => Ok("Order Updated!")), Duration.Inf)
            case None => InternalServerError(getOrderNotFoundMessage(orderId))
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }


  def deleteOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def delete = {
      orderRepository.getOrderByIdOption(orderId)
        .map({
          case Some(value) =>
            Await.result(orderRepository.deleteOrder(orderId)
              .map(_ => Ok("Removed order with id: " + orderId)), Duration.Inf)
          case None => InternalServerError(getOrderNotFoundMessage(orderId))
        })
    }

    silhouette.SecuredRequestHandler(HasRole(UserRoles.Admin)) { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Await.result(delete, Duration.Inf)
      case HandlerResult(r, None) => Unauthorized
    }
  }

  def deliverOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def deliver = {
      orderRepository.getOrderByIdOption(orderId)
        .map({
          case Some(value) =>
            Await.result(orderRepository.deliverOrder(orderId)
              .map(_ => Ok("Delivered order with id: " + orderId)), Duration.Inf)
          case None => InternalServerError(getOrderNotFoundMessage(orderId))
        })
    }

    silhouette.SecuredRequestHandler(HasRole(UserRoles.Admin)) { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Await.result(deliver, Duration.Inf)
      case HandlerResult(r, None) => Unauthorized
    }
  }

  private def getOrderNotFoundMessage(orderId: String) = {
    "Order with id: " + orderId + " does not exist!"
  }
}
