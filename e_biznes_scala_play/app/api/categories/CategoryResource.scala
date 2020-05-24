package api.categories

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import javax.inject.{Inject, Singleton}
import models.auth.UserRoles
import models.categories.Category
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc.{Action, _}
import repositories.categories.CategoryRepository
import utils.auth.{DefaultEnv, HasRole}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class CategoryResource @Inject()(categoryRepository: CategoryRepository,
                                 cc: MessagesControllerComponents,
                                 silhouette: Silhouette[DefaultEnv])
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getCategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryRepository.getCategories
      .map(categories => Ok(Json.toJson(categories)));
  }

  def getCategoryById(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryRepository.getCategoryByIdOption(categoryId)
      .map({
        case Some(category) => Ok(Json.toJson(category))
        case None => NotFound
      })
  }

  def createCategory: Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[Category] match {
      case JsSuccess(category, _) => categoryRepository.createCategory(category.name).map(_ => Ok("Category Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def updateCategory(categoryId: String): Action[JsValue] = silhouette.SecuredAction(HasRole(UserRoles.Admin)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue] =>
    request.body.validate[Category] match {
      case JsSuccess(category, _) =>
        categoryRepository.getCategoryByIdOption(categoryId)
          .map({
            case Some(value) =>
              val categoryToUpdate = Category(categoryId, category.name)
              Await.result(categoryRepository.updateCategory(categoryToUpdate)
                .map(_ => Ok("Category Updated!")), Duration.Inf)
            case None => InternalServerError("Category with id: " + categoryId + " does not exist!")
          })
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def deleteCategory(categoryId: String): Action[AnyContent] = Action.async { implicit request =>
    def delete: Future[Result] = {
      categoryRepository.getCategoryByIdOption(categoryId)
        .map({
          case Some(category) =>
            Await.result(categoryRepository.deleteCategory(categoryId)
              .map(_ => Ok("Removed category with id: " + categoryId)), Duration.Inf)
          case None => InternalServerError("Category with id: " + categoryId + " does not exist!")
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