package api.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.orders.{OrderRepository, PaymentRepository}

import scala.concurrent.ExecutionContext

@Singleton
class PaymentResource @Inject()(orderRepository: OrderRepository, paymentRepository: PaymentRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getPayments: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getPaymentById(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createPayment: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deletePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def finalizePayment(paymentId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }
}