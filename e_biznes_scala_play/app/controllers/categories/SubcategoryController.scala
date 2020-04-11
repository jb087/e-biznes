package controllers.categories

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import repositories.SubcategoryRepository

import scala.concurrent.ExecutionContext

@Singleton
class SubcategoryController @Inject()(subcategoryRepository: SubcategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getSubcategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val subcategories = subcategoryRepository.getSubcategories
    subcategories.map(subcategoriesFromFuture => Ok(views.html.subcategories.subcategories(subcategoriesFromFuture)))
  }

  def createSubcategory = Action {
    Ok("")
  }

  def updateSubcategory(subcategoryId: String) = Action {
    Ok("")
  }

  def deleteSubcategory(subcategoryId: String) = Action {
    Ok("")
  }
}
