package api.orders

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import repositories.orders.PaymentRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PaymentResource @Inject()(paymentRepository: PaymentRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getPayments: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.getPayments
      .map(payments => Ok(Json.toJson(payments)))
  }

  def getPaymentById(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.getPaymentByIdOption(paymentId)
      .map({
        case Some(payment) => Ok(Json.toJson(payment))
        case None => NotFound
      })
  }

  def createPayment(orderId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    try {
      val paymentId = Await.result(paymentRepository.createPayment(orderId), Duration.Inf)
      Future.successful(Ok(paymentId))
    } catch {
      case e: IllegalStateException => Future.successful(InternalServerError(e.getMessage))
    }
  }

  def deletePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    try {
      Await.result(paymentRepository.deletePayment(paymentId), Duration.Inf)
        Future.successful(Ok("Payment Delete!"))
    } catch {
      case e: IllegalStateException => Future.successful(InternalServerError(e.getMessage))
    }
  }

  def finalizePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    try {
      Await.result(paymentRepository.finalizePayment(paymentId), Duration.Inf)
      Future.successful(Ok("Payment Finalized!"))
    } catch {
      case e: IllegalStateException => Future.successful(InternalServerError(e.getMessage))
    }
  }
}