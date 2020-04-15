package controllers.orders

import javax.inject.{Inject, Singleton}
import models.basket.Basket
import models.orders.Order
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.basket.BasketRepository
import repositories.orders.OrderRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository, basketRepository: BasketRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createOrderForm: Form[CreateOrderForm] = Form {
    mapping(
      "basketId" -> nonEmptyText,
      "state" -> nonEmptyText
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "orderId" -> nonEmptyText,
      "basketId" -> nonEmptyText,
      "state" -> nonEmptyText
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
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
    val baskets: Seq[Basket] = Await.result(basketRepository.getBaskets(0), Duration.Inf)
    val basketIdsFromOrders: Seq[String] = Await.result(orderRepository.getOrders, Duration.Inf).map(_.basketId)
    val uniqueBaskets: Seq[Basket] = baskets.filter(basket => !basketIdsFromOrders.contains(basket.id))

    Future.successful {
      Ok(views.html.orders.orderadd(createOrderForm, uniqueBaskets))
    }
  }

  def createOrderHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createOrderForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val baskets: Seq[Basket] = Await.result(basketRepository.getBaskets(0), Duration.Inf)
          val basketIdsFromOrders: Seq[String] = Await.result(orderRepository.getOrders, Duration.Inf).map(_.basketId)
          val uniqueBaskets: Seq[Basket] = baskets.filter(basket => !basketIdsFromOrders.contains(basket.id))

          BadRequest(views.html.orders.orderadd(errorForm, uniqueBaskets))
        }
      },
      order => {
        orderRepository.createOrder(Order("", order.basketId, order.state))
          .map(_ => Redirect(routes.OrderController.createOrder()).flashing("success" -> "Order Created!"))
      }
    )
  }

  def updateOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val baskets: Seq[Basket] = Await.result(basketRepository.getBaskets(0), Duration.Inf)
    val basketIdsFromOrders: Seq[String] = Await.result(orderRepository.getOrders, Duration.Inf).map(_.basketId)
    val order: Order = Await.result(orderRepository.getOrderById(orderId), Duration.Inf)
    val basketIdsFromOrdersWithoutCurrent = basketIdsFromOrders.filter(id => !id.equals(order.basketId))
    val uniqueBaskets: Seq[Basket] = baskets.filter(basket => !basketIdsFromOrdersWithoutCurrent.contains(basket.id))

    orderRepository.getOrderById(orderId)
      .map(order => {
        val orderForm = updateOrderForm.fill(UpdateOrderForm(order.id, order.basketId, order.state))

        Ok(views.html.orders.orderupdate(orderForm, uniqueBaskets))
      })
  }

  def updateOrderHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateOrderForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val baskets: Seq[Basket] = Await.result(basketRepository.getBaskets(0), Duration.Inf)
          val basketIdsFromOrders: Seq[String] = Await.result(orderRepository.getOrders, Duration.Inf).map(_.basketId)
          val uniqueBaskets: Seq[Basket] = baskets.filter(basket => !basketIdsFromOrders.contains(basket.id))

          BadRequest(views.html.orders.orderupdate(errorForm, uniqueBaskets))
        }
      },
      order => {
        orderRepository.updateOrder(Order(order.orderId, order.basketId, order.state))
          .map(_ => Redirect(routes.OrderController.updateOrder(order.orderId)).flashing("success" -> "Order Updated!"))
      }
    )
  }

  def deleteOrder(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.deleteOrder(orderId)
      .map(_ => Redirect(routes.OrderController.getOrders()))
  }
}

case class CreateOrderForm(
                            basketId: String,
                            state: String
                          )

case class UpdateOrderForm(
                            orderId: String,
                            basketId: String,
                            state: String
                          )