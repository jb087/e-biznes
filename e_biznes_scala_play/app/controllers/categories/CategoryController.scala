package controllers.categories

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.CategoryRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }


  def getCategories = Action {
    Ok("")
  }

  def createCategory: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.categoryadd(categoryForm))
  }

  def createCategoryHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categoryadd(errorForm))
        )
      },
      category => {
        categoryRepository.create(category.name).map { _ =>
          Redirect(routes.CategoryController.createCategory()).flashing("success" -> "category.created")
        }
      }
    )
  }

  def updateCategory(categoryId: String) = Action {
    Ok("")
  }

  def deleteCategory(categoryId: String) = Action {
    Ok("")
  }
}

case class CreateCategoryForm(name: String)