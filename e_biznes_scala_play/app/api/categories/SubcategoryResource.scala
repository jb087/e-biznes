package api.categories

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.categories.{CategoryRepository, SubcategoryRepository}

import scala.concurrent.ExecutionContext

@Singleton
class SubcategoryResource @Inject()(categoryRepository: CategoryRepository, subcategoryRepository: SubcategoryRepository,
                                    cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getSubcategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getSubcategoryById(subcategoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createSubcategory: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateSubcategory(subcategoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteSubcategory(subcategoryId: String): Action[AnyContent] = Action {

  }
}