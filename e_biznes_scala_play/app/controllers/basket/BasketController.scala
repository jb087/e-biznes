package controllers.basket

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.basket.BasketRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketController @Inject()(basketRepository: BasketRepository, cc: MessagesControllerComponents)
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createBasketForm: Form[CreateBasketForm] = Form {
    mapping(
      "isBought" -> number
    )(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  def getBaskets: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    basketRepository.getBaskets(0)
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
    Ok(views.html.basket.basketadd(createBasketForm))
  }

  def createBasketHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createBasketForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          BadRequest(views.html.basket.basketadd(errorForm))
        }
      },
      basket => {
        basketRepository.createBasket(basket.isBought)
          .map(_ => Redirect(routes.BasketController.createBasket()).flashing("success" -> "Basket created!"))
      }
    )
  }

  def updateBasket(basketId: String) = Action {
    Ok("")
  }

  def deleteBasket(basketId: String) = Action {
    Ok("")
  }
}

case class CreateBasketForm(isBought: Int)
