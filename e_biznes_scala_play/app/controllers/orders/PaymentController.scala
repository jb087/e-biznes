package controllers.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import repositories.orders.{OrderRepository, PaymentRepository}

import scala.concurrent.ExecutionContext

@Singleton
class PaymentController @Inject()(orderRepository: OrderRepository, paymentRepository: PaymentRepository, cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

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

  def createPayment = Action {
    Ok("")
  }

  def createPaymentHandler = TODO

  def updatePayment(paymentId: String) = Action {
    Ok("")
  }

  def updatePaymentHandler = TODO

  def deletePayment(paymentId: String) = Action {
    Ok("")
  }
}
