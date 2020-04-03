package controllers.basket

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class BasketController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getBaskets = Action {
    Ok("")
  }

  def createBasket = Action {
    Ok("")
  }

  def updateBasket(basketId: String) = Action {
    Ok("")
  }

  def deleteBasket(basketId: String) = Action {
    Ok("")
  }
}
