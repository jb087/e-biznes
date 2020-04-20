package controllers.basket

import javax.inject.{Inject, Singleton}
import models.basket.Basket
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.basket.BasketRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketController @Inject()(basketRepository: BasketRepository, cc: MessagesControllerComponents)
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val updateBasketForm: Form[UpdateBasketForm] = Form {
    mapping(
      "basketId" -> nonEmptyText,
      "isBought" -> number
    )(UpdateBasketForm.apply)(UpdateBasketForm.unapply)
  }

  def getBaskets: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBaskets
      .map(baskets => Ok(views.html.basket.baskets(baskets)))
  }

  def getBasketById(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBasketByIdOption(basketId)
      .map({
        case Some(basket) => Ok(views.html.basket.basket(basket))
        case None => Redirect(routes.BasketController.getBaskets())
      })
  }

  def createBasket: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.basket.basketadd())
  }

  def createBasketHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.createBasket(0)
      .map(_ => Redirect(routes.BasketController.createBasket()).flashing("success" -> "Basket created!"))
  }

  def updateBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBasketById(basketId)
      .map(basket => {
        val basketForm = updateBasketForm.fill(UpdateBasketForm(basket.id, basket.isBought))

        Ok(views.html.basket.basketupdate(basketForm))
      })
  }

  def updateBasketHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateBasketForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          BadRequest(views.html.basket.basketupdate(errorForm))
        }
      },
      basket => {
        basketRepository.updateBasket(Basket(basket.basketId, basket.isBought))
          .map(_ => Redirect(routes.BasketController.updateBasket(basket.basketId)).flashing("success" -> "Basket updated!"))
      }
    )
  }

  def deleteBasket(basketId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.deleteBasket(basketId)
      .map(_ => Redirect(routes.BasketController.getBaskets()))
  }
}

case class UpdateBasketForm(
                             basketId: String,
                             isBought: Int
                           )
