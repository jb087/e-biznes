package api.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.orders.ShippingInformationRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingInformationResource @Inject()(shippingInformationRepository: ShippingInformationRepository, cc: MessagesControllerComponents)
                                           (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getShippingInformation: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def getShippingInformationById(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def createShippingInformation: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def updateShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def deleteShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }
}