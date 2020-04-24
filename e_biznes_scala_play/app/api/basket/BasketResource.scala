package api.basket

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.basket.BasketRepository

import scala.concurrent.ExecutionContext

@Singleton
class BasketResource @Inject()(basketRepository: BasketRepository, cc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getBaskets: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getBasketById(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBasketByIdOption(basketId)
      .map({
        case Some(basket) => Ok(views.html.basket.basket(basket))
        case None => Redirect(routes.BasketController.getBaskets())
      })
  }

  def createBasket: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }


  def deleteBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }
}