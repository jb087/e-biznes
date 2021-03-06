package api.basket

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.basket.Basket
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.basket.BasketRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class BasketResource @Inject()(basketRepository: BasketRepository,
                               cc: MessagesControllerComponents,
                               silhouette: Silhouette[DefaultEnv])
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getBaskets: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBaskets
      .map(baskets => Ok(Json.toJson(baskets)))
  }

  def getBoughtBasketsByLoggedUser: Action[AnyContent] = Action.async { implicit request =>
    silhouette.SecuredRequestHandler { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) =>
        val baskets = Await.result(basketRepository.getBasketsByUserId(user.userID), Duration.Inf)
        Ok(Json.toJson(baskets))
      case HandlerResult(r, None) => Unauthorized
    }
  }

  def getBasketById(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBasketByIdOption(basketId)
      .map({
        case Some(basket) => Ok(Json.toJson(basket))
        case None => NotFound
      })
  }

  def createBasket: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    silhouette.UserAwareRequestHandler { userAwareRequest =>
      Future.successful(HandlerResult(Ok, userAwareRequest.identity))
    }(request).flatMap {
      case HandlerResult(r, Some(user)) => {
        request.body.asJson match {
          case Some(json) => json.validate[Basket] match {
            case JsSuccess(basket, _) => basketRepository.createBasket(user.userID, basket.isBought).map(basketId => Ok(basketId))
            case _ => Future.successful(InternalServerError(getErrorMessage))
          }
          case None => Future.successful(InternalServerError(getErrorMessage))
        }
      }
      case HandlerResult(r, None) => request.body.asJson match {
        case Some(json) => json.validate[Basket] match {
          case JsSuccess(basket, _) => basketRepository.createBasket(basket.isBought).map(basketId => Ok(basketId))
          case _ => Future.successful(InternalServerError(getErrorMessage))
        }
        case None => Future.successful(InternalServerError(getErrorMessage))
      }
    }
  }

  def updateBasket(basketId: String): Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[Basket] match {
      case JsSuccess(basket, _) =>
        basketRepository.getBasketByIdOption(basketId)
          .map({
            case Some(basketFromRepo) =>
              val basketToUpdate = Basket(basketId, basketFromRepo.userId, basket.isBought)
              Await.result(basketRepository.updateBasket(basketToUpdate)
                .map(_ => Ok("Basket Updated!")), Duration.Inf)
            case None => InternalServerError("Basket with id: " + basketId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError(getErrorMessage))
    }
  }


  def deleteBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def delete = {
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

    silhouette.SecuredRequestHandler(HasRole(UserRoles.Admin)) { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Await.result(delete, Duration.Inf)
      case HandlerResult(r, None) => Unauthorized
    }
  }

  private def getErrorMessage = {
    "Provided body is not valid. Please provide correct body with empty id."
  }
}