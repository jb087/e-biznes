package controllers.categories

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getCategories = Action {
    Ok("")
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
