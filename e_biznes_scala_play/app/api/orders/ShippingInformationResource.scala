package api.orders

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.orders.ShippingInformation
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.orders.ShippingInformationRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ShippingInformationResource @Inject()(shippingInformationRepository: ShippingInformationRepository,
                                            cc: MessagesControllerComponents,
                                            silhouette: Silhouette[DefaultEnv])
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
      case JsSuccess(shippingInformation, _) => shippingInformationRepository.createShippingInformation(shippingInformation)
        .map(informationId => Ok(informationId))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateShippingInformation(shippingInformationId: String): Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[ShippingInformation] match {
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
    def delete = {
      shippingInformationRepository.getShippingInformationByIdOption(shippingInformationId)
        .map({
          case Some(value) =>
            Await.result(shippingInformationRepository.deleteShippingInformation(shippingInformationId)
              .map(_ => Ok("Removed shipping information with id: " + shippingInformationId)), Duration.Inf)
          case None => InternalServerError("Shipping Information with id: " + shippingInformationId + " does not exist!")
        })
    }

    silhouette.SecuredRequestHandler(HasRole(UserRoles.Admin)) { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Await.result(delete, Duration.Inf)
      case HandlerResult(r, None) => Unauthorized
    }
  }
}