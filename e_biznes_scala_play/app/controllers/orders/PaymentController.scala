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
          .map(_ => Redirect(routes.PaymentController.createPayment()).flashing("success" -> "Order Created!"))
      }
    )
  }

  def updatePayment(paymentId: String) = Action {
    Ok("")
  }

  def updatePaymentHandler = TODO

  def deletePayment(paymentId: String) = Action {
    Ok("")
  }
}

case class CreatePaymentForm(orderId: String)