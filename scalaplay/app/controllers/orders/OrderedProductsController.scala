package controllers.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class OrderedProductsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getOrderedProducts = Action {
    Ok("")
  }

  def createOrderedProduct = Action {
    Ok("")
  }

  def updateOrderedProduct(orderedProductId: String) = Action {
    Ok("")
  }

  def deleteOrderedProduct(orderedProductId: String) = Action {
    Ok("")
  }
}
