package controllers.categories

import javax.inject.{Inject, Singleton}
import models.Category
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.CategoryRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createCategoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "categoryId" -> nonEmptyText,
      "name" -> nonEmptyText
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def getCategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepository.getCategories
    categories.map(categoriesFromFuture => Ok(views.html.categories.categories(categoriesFromFuture)))
  }

  def createCategory: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.categories.categoryadd(createCategoryForm))
  }

  def createCategoryHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createCategoryForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categories.categoryadd(errorForm))
        )
      },
      category => {
        categoryRepository.createCategory(category.name).map { _ =>
          Redirect(routes.CategoryController.createCategory()).flashing("success" -> "Category created!")
        }
      }
    )
  }

  def updateCategory(categoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepository.getCategoryById(categoryId)
    category.map(categoryFromFuture => {
      val categoryForm = updateCategoryForm.fill(UpdateCategoryForm(categoryFromFuture.id, categoryFromFuture.name))
      Ok(views.html.categories.categoryupdate(categoryForm))
    })
  }

  def updateCategoryHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateCategoryForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categories.categoryupdate(errorForm))
        )
      },
      category => {
        categoryRepository.updateCategory(Category(category.categoryId, category.name)).map { _ =>
          Redirect(routes.CategoryController.updateCategory(category.categoryId)).flashing("success" -> "Category updated!")
        }
      }
    )
  }

  def deleteCategory(categoryId: String): Action[AnyContent] = Action {
    categoryRepository.deleteCategory(categoryId)
    Redirect(routes.CategoryController.getCategories())
  }
}

case class CreateCategoryForm(name: String)

case class UpdateCategoryForm(categoryId: String, name: String)