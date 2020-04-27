package api.basket

import javax.inject.{Inject, Singleton}
import models.basket.Basket
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.basket.BasketRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class BasketResource @Inject()(basketRepository: BasketRepository, cc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getBaskets: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBaskets
      .map(baskets => Ok(Json.toJson(baskets)))
  }

  def getBasketById(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBasketByIdOption(basketId)
      .map({
        case Some(basket) => Ok(Json.toJson(basket))
        case None => NotFound
      })
  }

  def createBasket: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Basket] match {
      case JsSuccess(basket, _) => basketRepository.createBasket(basket.isBought).map(_ => Ok("Basket Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateBasket(basketId: String): Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Basket] match {
      case JsSuccess(basket, _) =>
        basketRepository.getBasketByIdOption(basketId)
          .map({
            case Some(value) =>
              val basketToUpdate = Basket(basketId, basket.isBought)
              Await.result(basketRepository.updateBasket(basketToUpdate)
                .map(_ => Ok("Basket Updated!")), Duration.Inf)
            case None => InternalServerError("Basket with id: " + basketId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }


  def deleteBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBasketByIdOption(basketId)
      .map({
        case Some(basket) =>
          if (basket.isBought != 0) {
            Ok("Could not remove basket with id: " + basketId + ", because basket is bought!")
          } else {
            Await.result(basketRepository.deleteBasket(basketId)
              .map(_ => Ok("Removed basket with id: " + basketId)), Duration.Inf)
          }
        case None => InternalServerError("Basket with id: " + basketId + " does not exist!")
      })
  }
}