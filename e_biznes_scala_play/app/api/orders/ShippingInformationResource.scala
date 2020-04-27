package api.orders

import javax.inject.{Inject, Singleton}
import models.orders.ShippingInformation
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.orders.ShippingInformationRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ShippingInformationResource @Inject()(shippingInformationRepository: ShippingInformationRepository, cc: MessagesControllerComponents)
                                           (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getShippingInformation: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.getShippingInformation
      .map(shippingInformation => Ok(Json.toJson(shippingInformation)))
  }

  def getShippingInformationById(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.getShippingInformationByIdOption(shippingInformationId)
      .map({
        case Some(shippingInformation) => Ok(Json.toJson(shippingInformation))
        case None => NotFound
      })
  }

  def createShippingInformation: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[ShippingInformation] match {
      case JsSuccess(shippingInformation, _) => shippingInformationRepository.createShippingInformation(shippingInformation).map(_ => Ok("Shipping Information Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateShippingInformation(shippingInformationId: String): Action[JsValue] = Action.async(parse.json) {
    _.body.validate[ShippingInformation] match {
      case JsSuccess(shippingInformation, _) =>
        shippingInformationRepository.getShippingInformationByIdOption(shippingInformationId)
          .map({
            case Some(value) =>
              val shippingInformationToUpdate = ShippingInformation(shippingInformationId, shippingInformation.firstName, shippingInformation.lastName,
                shippingInformation.email, shippingInformation.street, shippingInformation.houseNumber, shippingInformation.city, shippingInformation.zipCode)
              Await.result(shippingInformationRepository.updateShippingInformation(shippingInformationToUpdate)
                .map(_ => Ok("Shipping Information Updated!")), Duration.Inf)
            case None => InternalServerError("Shipping Information with id: " + shippingInformationId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def deleteShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.getShippingInformationByIdOption(shippingInformationId)
      .map({
        case Some(value) =>
          Await.result(shippingInformationRepository.deleteShippingInformation(shippingInformationId)
            .map(_ => Ok("Removed shipping information with id: " + shippingInformationId)), Duration.Inf)
        case None => InternalServerError("Shipping Information with id: " + shippingInformationId + " does not exist!")
      })
  }
}