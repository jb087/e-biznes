package controllers.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class PaymentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getPayments = Action {
    Ok("")
  }

  def createPayment = Action {
    Ok("")
  }

  def updatePayment(paymentId: String) = Action {
    Ok("")
  }

  def deletePayment(paymentId: String) = Action {
    Ok("")
  }
}
