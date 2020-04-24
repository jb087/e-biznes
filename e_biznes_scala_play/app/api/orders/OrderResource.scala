package api.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.basket.BasketRepository
import repositories.orders.{OrderRepository, ShippingInformationRepository}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderResource @Inject()(orderRepository: OrderRepository, basketRepository: BasketRepository, shippingInformationRepository: ShippingInformationRepository,
                              cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOrders: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def getOrderById(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def createOrder: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def updateOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def deleteOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }
}