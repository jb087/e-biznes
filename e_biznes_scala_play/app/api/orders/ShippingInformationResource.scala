package api.orders

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.orders.ShippingInformationRepository

import scala.concurrent.ExecutionContext

@Singleton
class ShippingInformationResource @Inject()(shippingInformationRepository: ShippingInformationRepository, cc: MessagesControllerComponents)
                                           (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getShippingInformation: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getShippingInformationById(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createShippingInformation: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateShippingInformationHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }
}