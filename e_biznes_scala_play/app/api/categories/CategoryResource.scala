package api.categories

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.categories.CategoryRepository

import scala.concurrent.ExecutionContext

@Singleton
class CategoryResource @Inject()(categoryRepository: CategoryRepository, cc: MessagesControllerComponents)
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getCategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getCategoryById(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createCategory: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateCategory(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteCategory(categoryId: String): Action[AnyContent] = Action {

  }
}