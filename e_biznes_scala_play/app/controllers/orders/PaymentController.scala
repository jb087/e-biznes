package controllers.orders

import java.time.LocalDate

import javax.inject.{Inject, Singleton}
import models.orders.Payment
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.orders.{OrderRepository, PaymentRepository}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PaymentController @Inject()(orderRepository: OrderRepository, paymentRepository: PaymentRepository, cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createPaymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "orderId" -> nonEmptyText,
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "paymentId" -> nonEmptyText,
      "orderId" -> nonEmptyText,
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  def getPayments: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.getPayments
      .map(payments => Ok(views.html.payments.payments(payments)))
  }

  def getPaymentById(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.getPaymentByIdOption(paymentId)
      .map({
        case Some(payment) => Ok(views.html.payments.payment(payment))
        case None => Redirect(routes.PaymentController.getPayments())
      })
  }

  def createPayment: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val orders = Await.result(orderRepository.getOrders, Duration.Inf)
    val ordersWithPayment = Await.result(paymentRepository.getPayments, Duration.Inf).map(_.orderId)
    val ordersWithoutPayment = orders.filter(orders => !ordersWithPayment.contains(orders.id))

    Future.successful {
      Ok(views.html.payments.paymentadd(createPaymentForm, ordersWithoutPayment))
    }
  }

  def createPaymentHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createPaymentForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val orders = Await.result(orderRepository.getOrders, Duration.Inf)

          BadRequest(views.html.payments.paymentadd(errorForm, orders))
        }
      },
      payment => {
        paymentRepository.createPayment(Payment("", payment.orderId, 0, LocalDate.now()))
          .map(_ => Redirect(routes.PaymentController.createPayment()).flashing("success" -> "Payment Created!"))
      }
    )
  }

  def updatePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val orders = Await.result(orderRepository.getOrders, Duration.Inf)
    val ordersWithPayment = Await.result(paymentRepository.getPayments, Duration.Inf).map(_.orderId)
    val payment = Await.result(paymentRepository.getPaymentById(paymentId), Duration.Inf)
    val ordersWithPaymentWithoutCurrent = ordersWithPayment.filter(id => !id.equals(payment.orderId))
    val ordersWithoutPayment = orders.filter(orders => !ordersWithPaymentWithoutCurrent.contains(orders.id))

    Future.successful {
      val paymentForm = updatePaymentForm.fill(UpdatePaymentForm(payment.id, payment.orderId))

      Ok(views.html.payments.paymentupdate(paymentForm, ordersWithoutPayment))
    }
  }

  def updatePaymentHandler : Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatePaymentForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val orders = Await.result(orderRepository.getOrders, Duration.Inf)
          val ordersWithPayment = Await.result(paymentRepository.getPayments, Duration.Inf).map(_.orderId)
          val payment = Await.result(paymentRepository.getPaymentById(errorForm.data("paymentId")), Duration.Inf)
          val ordersWithPaymentWithoutCurrent = ordersWithPayment.filter(id => !id.equals(payment.orderId))
          val ordersWithoutPayment = orders.filter(orders => !ordersWithPaymentWithoutCurrent.contains(orders.id))

          BadRequest(views.html.payments.paymentupdate(errorForm, ordersWithoutPayment))
        }
      },
      payment => {
        paymentRepository.updatePayment(Payment(payment.paymentId, payment.orderId, 0, LocalDate.now()))
          .map(_ => Redirect(routes.PaymentController.updatePayment(payment.paymentId)).flashing("success" -> "Payment Updated!"))
      }
    )
  }

  def deletePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.deletePayment(paymentId)
      .map(_ => Redirect(routes.PaymentController.getPayments()))
  }
}

case class CreatePaymentForm(orderId: String)

case class UpdatePaymentForm(
                              paymentId: String,
                              orderId: String
                            )