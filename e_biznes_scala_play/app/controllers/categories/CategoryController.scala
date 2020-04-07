package controllers.categories

import dao.CategoryDAO
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}
import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class CategoryController @Inject()(cc: ControllerComponents, categoryDAO: CategoryDAO) extends AbstractController(cc) {

  def getCategories = Action.async {
    categoryDAO.all().map(category => Ok(category.toString()))
  }

  def createCategory = Action {
    Ok("")
  }

  def updateCategory(categoryId: String) = Action {
    Ok("")
  }

  def deleteCategory(categoryId: String) = Action {
    Ok("")
  }
}
