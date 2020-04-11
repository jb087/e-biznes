package controllers.categories

import javax.inject.{Inject, Singleton}
import models.Category
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.{CategoryRepository, SubcategoryRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class SubcategoryController @Inject()(categoryRepository: CategoryRepository, subcategoryRepository: SubcategoryRepository,
                                      cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createSubcategoryForm: Form[CreateSubcategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "categoryId" -> nonEmptyText
    )(CreateSubcategoryForm.apply)(CreateSubcategoryForm.unapply)
  }

  def getSubcategories: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val subcategories = subcategoryRepository.getSubcategories
    subcategories.map(subcategoriesFromFuture => Ok(views.html.subcategories.subcategories(subcategoriesFromFuture)))
  }

  def getSubcategoryById(subcategoryId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val subcategory = subcategoryRepository.getSubcategoryByIdOption(subcategoryId)
    subcategory.map(subcategoryFromFuture => subcategoryFromFuture match {
      case Some(subcategoryFromFuture) => Ok(views.html.subcategories.subcategory(subcategoryFromFuture))
      case None => Redirect(routes.SubcategoryController.getSubcategories())
    })
  }

  def createSubcategory: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepository.getCategories
    categories.map(categoriesFromFuture => Ok(views.html.subcategories.subcategoryadd(createSubcategoryForm, categoriesFromFuture)))
  }

  def createSubcategoryHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createSubcategoryForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          var categories: Seq[Category] = Seq[Category]()
          categoryRepository.getCategories.onComplete {
            case Success(categoriesFromFuture) => categories = categoriesFromFuture
            case Failure(_) => print("Failed categories download")
          }

          BadRequest(views.html.subcategories.subcategoryadd(errorForm, categories))
        }
      },
      subcategory => {
        subcategoryRepository.createSubcategory(subcategory.categoryId, subcategory.name).map { _ =>
          Redirect(routes.SubcategoryController.createSubcategory()).flashing("success" -> "Subcategory created!")
        }
      }
    )
  }

  def updateSubcategory(subcategoryId: String) = Action {
    Ok("")
  }

  def deleteSubcategory(subcategoryId: String) = Action {
    Ok("")
  }
}

case class CreateSubcategoryForm(name: String, categoryId: String)
