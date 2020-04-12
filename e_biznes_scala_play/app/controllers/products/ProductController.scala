package controllers.products

import java.time.LocalDate

import javax.inject.{Inject, Singleton}
import models.categories.Subcategory
import models.products.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc._
import repositories.categories.SubcategoryRepository
import repositories.products.ProductRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductController @Inject()(productRepository: ProductRepository, subcategoryRepository: SubcategoryRepository,
                                  cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createProductForm: Form[CreateProductForm] = Form {
    mapping(
      "subcategoryId" -> nonEmptyText,
      "title" -> nonEmptyText,
      "price" -> of(doubleFormat),
      "description" -> text,
      "quantity" -> number
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "productId" -> nonEmptyText,
      "subcategoryId" -> nonEmptyText,
      "title" -> nonEmptyText,
      "price" -> of(doubleFormat),
      "description" -> text,
      "quantity" -> number
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def getProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProducts
      .map(products => Ok(views.html.products.products(products)))
  }

  def getProductById(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProductByIdOption(productId)
      .map {
        case Some(product) => Ok(views.html.products.product(product))
        case None => Redirect(routes.ProductController.getProducts())
      }
  }

  def createProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    subcategoryRepository.getSubcategories
      .map(subcategories => Ok(views.html.products.productadd(createProductForm, subcategories)))
  }

  def createProductHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createProductForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          var subcategories: Seq[Subcategory] = Seq[Subcategory]()
          subcategoryRepository.getSubcategories.onComplete {
            case Success(subcategoriesFromFuture) => subcategories = subcategoriesFromFuture
            case Failure(_) => print("Failed subcategories download")
          }

          BadRequest(views.html.products.productadd(errorForm, subcategories))
        }
      },
      product => {
        productRepository.createProduct(Product("", product.subcategoryId, product.title, product.price, product.description, LocalDate.now(), product.quantity))
          .map( _ => Redirect(routes.ProductController.createProduct()).flashing("success" -> "Product created!"))
      }
    )
  }

  def updateProduct(productId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var subcategories: Seq[Subcategory] = Seq[Subcategory]()
    subcategoryRepository.getSubcategories.onComplete {
      case Success(subcategoriesFromFuture) => subcategories = subcategoriesFromFuture
      case Failure(_) => print("Failed subcategories download")
    }

    productRepository.getProductById(productId)
      .map(product => {
        val productForm = updateProductForm.fill(
          UpdateProductForm(product.id, product.subcategoryId, product.title, product.price, product.description, product.quantity)
        )

        Ok(views.html.products.productupdate(productForm, subcategories))
      })
  }

  def updateProductHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateProductForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          var subcategories: Seq[Subcategory] = Seq[Subcategory]()
          subcategoryRepository.getSubcategories.onComplete {
            case Success(subcategoriesFromFuture) => subcategories = subcategoriesFromFuture
            case Failure(_) => print("Failed subcategories download")
          }

          BadRequest(views.html.products.productupdate(errorForm, subcategories))
        }
      },
      product => {
        productRepository.updateProduct(Product(product.productId, product.subcategoryId, product.title, product.price, product.description, LocalDate.now(), product.quantity))
          .map(_ => Redirect(routes.ProductController.updateProduct(product.productId)).flashing("success" -> "Product updated!"))
      }
    )
  }

  def deleteProduct(productId: String): Action[AnyContent] = Action {
    productRepository.deleteProduct(productId)
    Redirect(routes.ProductController.getProducts())
  }
}

case class CreateProductForm(
                              subcategoryId: String,
                              title: String,
                              price: Double,
                              description: String,
                              quantity: Int
                            )

case class UpdateProductForm(
                              productId: String,
                              subcategoryId: String,
                              title: String,
                              price: Double,
                              description: String,
                              quantity: Int
                            )