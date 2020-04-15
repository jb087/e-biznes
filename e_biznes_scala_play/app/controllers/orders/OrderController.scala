package controllers.orders

import javax.inject.{Inject, Singleton}
import models.basket.Basket
import models.orders.Order
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.basket.BasketRepository
import repositories.orders.OrderRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository, basketRepository: BasketRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createOrderForm: Form[CreateOrderForm] = Form {
    mapping(
      "basketId" -> nonEmptyText,
      "state" -> nonEmptyText
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def getOrders: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getOrders
      .map(orders => Ok(views.html.orders.orders(orders)))
  }

  def getOrderById(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getOrderByIdOption(orderId)
      .map({
        case Some(order) => Ok(views.html.orders.order(order))
        case None => Redirect(routes.OrderController.getOrders())
      })
  }

  def createOrder: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBaskets(0)
      .map(baskets => Ok(views.html.orders.orderadd(createOrderForm, baskets)))
  }

  def createOrderHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createOrderForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          var baskets: Seq[Basket] = Seq[Basket]()
          basketRepository.getBaskets(0).onComplete {
            case Success(basketsFromFuture) => baskets = basketsFromFuture
            case Failure(_) => print("Failed baskets download")
          }

          BadRequest(views.html.orders.orderadd(errorForm, baskets))
        }
      },
      order => {
        orderRepository.createOrder(Order("", order.basketId, order.state))
          .map(_ => Redirect(routes.OrderController.createOrder()).flashing("success" -> "Order Created!"))
      }
    )
  }

  def updateOrder(orderId: String) = Action {
    Ok("")
  }

  def deleteOrder(orderId: String) = Action {
    Ok("")
  }
}

case class CreateOrderForm(
                            basketId: String,
                            state: String
                          )