package api.orders

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import repositories.orders.PaymentRepository

import scala.concurrent.ExecutionContext

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
    paymentRepository.createPayment(orderId)
      .map(_ => Ok("Payment Created!"))
  }

  def deletePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.deletePayment(paymentId)
      .map(_ => Ok("Payment Delete!"))
  }

  def finalizePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.finalizePayment(paymentId)
      .map(_ => Ok("Payment Finalized!"))
  }
}