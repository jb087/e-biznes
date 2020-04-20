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
        try {
          Await.result(paymentRepository.createPayment(Payment("", payment.orderId, 0, LocalDate.now())), Duration.Inf)
          Future.successful {
            Redirect(routes.PaymentController.createPayment()).flashing("info" -> "Payment Created!")
          }
        } catch {
          case e: IllegalStateException => Future.successful {
            Redirect(routes.PaymentController.createPayment()).flashing("info" -> e.getMessage)
          }
        }
      }
    )
  }

  def deletePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    try {
      paymentRepository.deletePayment(paymentId)
        .map(_ => Redirect(routes.PaymentController.getPayments()))
    } catch {
      case e: IllegalStateException => Future.successful {
        Redirect(routes.PaymentController.getPayments()).flashing("info" -> e.getMessage)
      }
    }
  }

  def finalizePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    try {
      Await.result(paymentRepository.finalizePayment(paymentId), Duration.Inf)
      Future.successful {
        Redirect(routes.PaymentController.getPayments())
      }
    } catch {
      case e: IllegalStateException => Future.successful {
        Redirect(routes.PaymentController.getPayments()).flashing("info" -> e.getMessage)
      }
    }
  }
}

case class CreatePaymentForm(orderId: String)