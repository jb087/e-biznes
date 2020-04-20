package controllers.basket

import javax.inject.{Inject, Singleton}
import models.basket.OrderedProduct
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.basket.{BasketRepository, OrderedProductRepository}
import repositories.products.ProductRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderedProductsController @Inject()(orderedProductRepository: OrderedProductRepository, basketRepository: BasketRepository,
                                          productRepository: ProductRepository, cc: MessagesControllerComponents)
                                         (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createOrderedProductForm: Form[CreateOrderedProductForm] = Form {
    mapping(
      "basketId" -> nonEmptyText,
      "productId" -> nonEmptyText,
      "quantity" -> number
    )(CreateOrderedProductForm.apply)(CreateOrderedProductForm.unapply)
  }

  val updateOrderedProductForm: Form[UpdateOrderedProductForm] = Form {
    mapping(
      "orderedProductId" -> nonEmptyText,
      "basketId" -> nonEmptyText,
      "productId" -> nonEmptyText,
      "quantity" -> number
    )(UpdateOrderedProductForm.apply)(UpdateOrderedProductForm.unapply)
  }

  def getOrderedProducts: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val baskets = Await.result(basketRepository.getBaskets, Duration.Inf)

    orderedProductRepository.getOrderedProducts
      .map(orderedProducts => Ok(views.html.orderedProducts.orderedproducts(orderedProducts, baskets)))
  }

  def getOrderedProductById(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderedProductRepository.getOrderedProductByIdOption(orderedProductId)
      .map({
        case Some(orderedProduct) => Ok(views.html.orderedProducts.orderedproduct(orderedProduct))
        case None => Redirect(routes.OrderedProductsController.getOrderedProducts())
      })
  }

  def createOrderedProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = Await.result(productRepository.getProducts, Duration.Inf)

    basketRepository.getBaskets(0)
      .map(baskets => Ok(views.html.orderedProducts.orderedproductadd(createOrderedProductForm, baskets, products)))
  }

  def createOrderedProductHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createOrderedProductForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val products = Await.result(productRepository.getProducts, Duration.Inf)
          val baskets = Await.result(basketRepository.getBaskets(0), Duration.Inf)

          BadRequest(views.html.orderedProducts.orderedproductadd(errorForm, baskets, products))
        }
      },
      orderedProduct => {
        orderedProductRepository.createOrderedProduct(OrderedProduct("", orderedProduct.basketId, orderedProduct.productId, orderedProduct.quantity))
          .map(_ => Redirect(routes.OrderedProductsController.createOrderedProduct()).flashing("success" -> "Ordered Product Created!"))
      }
    )
  }

  def updateOrderedProduct(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = Await.result(productRepository.getProducts, Duration.Inf)
    val baskets = Await.result(basketRepository.getBaskets(0), Duration.Inf)

    orderedProductRepository.getOrderedProductById(orderedProductId)
      .map(orderedProduct => {
        val orderedProductForm = updateOrderedProductForm.fill(
          UpdateOrderedProductForm(orderedProduct.id, orderedProduct.basketId, orderedProduct.productId, orderedProduct.quantity)
        )

        Ok(views.html.orderedProducts.orderedproductupdate(orderedProductForm, baskets, products))
      })
  }

  def updateOrderedProductHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateOrderedProductForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val products = Await.result(productRepository.getProducts, Duration.Inf)
          val baskets = Await.result(basketRepository.getBaskets(0), Duration.Inf)

          BadRequest(views.html.orderedProducts.orderedproductupdate(errorForm, baskets, products))
        }
      },
      orderedProduct => {
        try {
          orderedProductRepository.updateOrderedProduct(
            OrderedProduct(orderedProduct.orderedProductId, orderedProduct.basketId, orderedProduct.productId, orderedProduct.quantity)
          )
            .map(_ => Redirect(routes.OrderedProductsController.updateOrderedProduct(orderedProduct.orderedProductId)).flashing("info" -> "Ordered Product Updated!"))
        } catch {
          case e: IllegalStateException => Future.successful {
            Redirect(routes.OrderedProductsController.updateOrderedProduct(orderedProduct.orderedProductId)).flashing("info" -> e.getMessage)
          }
        }
      }
    )
  }

  def deleteOrderedProduct(orderedProductId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    try {
      orderedProductRepository.deleteOrderedProduct(orderedProductId)
        .map(_ => Redirect(routes.OrderedProductsController.getOrderedProducts()))
    } catch {
      case e: IllegalStateException => Future.successful {
        Redirect(routes.OrderedProductsController.getOrderedProducts()).flashing("info" -> e.getMessage)
      }
    }
  }
}

case class CreateOrderedProductForm(
                                     basketId: String,
                                     productId: String,
                                     quantity: Int
                                   )

case class UpdateOrderedProductForm(
                                     orderedProductId: String,
                                     basketId: String,
                                     productId: String,
                                     quantity: Int
                                   )