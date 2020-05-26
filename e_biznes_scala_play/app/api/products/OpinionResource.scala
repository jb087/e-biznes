package api.products

import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.products.Opinion
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.products.OpinionRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OpinionResource @Inject()(opinionRepository: OpinionRepository,
                                cc: MessagesControllerComponents,
                                silhouette: Silhouette[DefaultEnv])
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOpinions: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    opinionRepository.getOpinions
      .map(opinions => Ok(Json.toJson(opinions)))
  }

  def getOpinionsByProductId(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    opinionRepository.getOpinionsByProductId(productId)
      .map(opinions => Ok(Json.toJson(opinions)))
  }

  def getOpinionById(opinionId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    opinionRepository.getOpinionByIdOption(opinionId)
      .map({
        case Some(opinion) => Ok(Json.toJson(opinion))
        case None => NotFound
      })
  }

  def createOpinion: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Opinion] match {
      case JsSuccess(opinion, _) => opinionRepository.createOpinion(opinion).map(_ => Ok("Opinion Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateOpinion(opinionId: String): Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[Opinion] match {
      case JsSuccess(opinion, _) =>
        opinionRepository.getOpinionByIdOption(opinionId)
          .map({
            case Some(value) =>
              val opinionToUpdate = Opinion(opinionId, opinion.productId, opinion.opinion, opinion.rating)
              Await.result(opinionRepository.updateOpinion(opinionToUpdate)
                .map(_ => Ok("Opinion Updated!")), Duration.Inf)
            case None => InternalServerError("Opinion with id: " + opinionId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def deleteOpinion(opinionId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def delete = {
      opinionRepository.getOpinionByIdOption(opinionId)
        .map({
          case Some(value) =>
            Await.result(opinionRepository.deleteOpinion(opinionId)
              .map(_ => Ok("Removed opinion with id: " + opinionId)), Duration.Inf)
          case None => InternalServerError("Opinion with id: " + opinionId + " does not exist!")
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