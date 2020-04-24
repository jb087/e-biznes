package api.basket

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.basket.BasketRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketResource @Inject()(basketRepository: BasketRepository, cc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getBaskets: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def getBasketById(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def createBasket: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def updateBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }


  def deleteBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }
}