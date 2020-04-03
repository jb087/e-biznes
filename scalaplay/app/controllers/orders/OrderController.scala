package controllers.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class OrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getOrders = Action {
    Ok("")
  }

  def createOrder = Action {
    Ok("")
  }

  def updateOrder(orderId: String) = Action {
    Ok("")
  }

  def deleteOrder(orderId: String) = Action {
    Ok("")
  }
}
