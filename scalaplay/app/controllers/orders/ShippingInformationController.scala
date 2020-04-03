package controllers.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class ShippingInformationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getShippingInformation = Action {
    Ok("")
  }

  def createShippingInformation = Action {
    Ok("")
  }

  def updateShippingInformation(shippingInformationId: String) = Action {
    Ok("")
  }

  def deleteShippingInformation(shippingInformationId: String) = Action {
    Ok("")
  }
}
