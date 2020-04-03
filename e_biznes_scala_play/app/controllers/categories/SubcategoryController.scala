package controllers.categories

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class SubcategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getSubcategories = Action {
    Ok("")
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
