package api.categories

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import repositories.categories.CategoryRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryResource @Inject()(categoryRepository: CategoryRepository, cc: MessagesControllerComponents)
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getCategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryRepository.getCategories
      .map(categories => Ok(Json.toJson(categories)));
  }

  def getCategoryById(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def createCategory: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def updateCategory(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }

  def deleteCategory(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok("")
    }
  }
}