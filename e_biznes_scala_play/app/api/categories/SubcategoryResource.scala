package api.categories

import javax.inject.{Inject, Singleton}
import models.categories.Subcategory
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.categories.SubcategoryRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class SubcategoryResource @Inject()(subcategoryRepository: SubcategoryRepository, cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getSubcategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    subcategoryRepository.getSubcategories
      .map(subcategories => Ok(Json.toJson(subcategories)))
  }

  def getSubcategoryById(subcategoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    subcategoryRepository.getSubcategoryByIdOption(subcategoryId)
      .map({
        case Some(subcategory) => Ok(Json.toJson(subcategory))
        case None => NotFound
      })
  }

  def createSubcategory: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Subcategory] match {
      case JsSuccess(subcategory, _) => subcategoryRepository.createSubcategory(subcategory.parentId, subcategory.name).map(_ => Ok("Subcategory Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateSubcategory(subcategoryId: String): Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Subcategory] match {
      case JsSuccess(subcategory, _) =>
        subcategoryRepository.getSubcategoryByIdOption(subcategoryId)
          .map({
            case Some(value) =>
              val subcategoryToUpdate = Subcategory(subcategoryId, subcategory.parentId, subcategory.name)
                Await.result(subcategoryRepository.updateSubcategory(subcategoryToUpdate)
                  .map(_ => Ok("Subcategory Updated!")), Duration.Inf)
            case None => InternalServerError("Subcategory with id: " + subcategoryId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def deleteSubcategory(subcategoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    subcategoryRepository.getSubcategoryByIdOption(subcategoryId)
      .map({
        case Some(subcategory) =>
          Await.result(subcategoryRepository.deleteSubcategory(subcategoryId)
            .map(_ => Ok("Removed subcategory with id: " + subcategoryId)), Duration.Inf)
        case None => InternalServerError("Subcategory with id: " + subcategoryId + " does not exist!")
      })
  }
}