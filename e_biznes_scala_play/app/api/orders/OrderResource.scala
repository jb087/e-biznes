package api.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.basket.BasketRepository
import repositories.orders.{OrderRepository, ShippingInformationRepository}

import scala.concurrent.ExecutionContext

@Singleton
class OrderResource @Inject()(orderRepository: OrderRepository, basketRepository: BasketRepository, shippingInformationRepository: ShippingInformationRepository,
                              cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOrders: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getOrderById(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createOrder: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }
}