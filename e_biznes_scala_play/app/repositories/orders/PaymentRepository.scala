package repositories.orders

import java.time.LocalDate
import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.basket.{Basket, BasketTable, OrderedProduct}
import models.orders.{Order, OrderTable, Payment, PaymentTable}
import models.products.{Product, ProductTable}
import play.api.db.slick.DatabaseConfigProvider
import repositories.basket.{BasketRepository, OrderedProductRepository}
import repositories.products.ProductRepository
import slick.jdbc.JdbcProfile

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(
                                   productRepository: ProductRepository,
                                   orderedProductRepository: OrderedProductRepository,
                                   basketRepository: BasketRepository,
                                   orderRepository: OrderRepository,
                                   dbConfigProvider: DatabaseConfigProvider
                                 )
                                 (implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val payment = TableQuery[PaymentTable]
  private val order = TableQuery[OrderTable]
  private val basket = TableQuery[BasketTable]
  private val product = TableQuery[ProductTable]

  def getPayments: Future[Seq[Payment]] = db.run {
    payment.result
  }

  def getPaymentById(paymentId: String): Future[Payment] = db.run {
    payment.filter(_.id === paymentId).result.head
  }

  def getPaymentByIdOption(paymentId: String): Future[Option[Payment]] = db.run {
    payment.filter(_.id === paymentId).result.headOption
  }

  def createPayment(newPayment: Payment): Future[Unit] = {
    val id = UUID.randomUUID().toString
    val order = Await.result(orderRepository.getOrderById(newPayment.orderId), Duration.Inf)
    val basket = Await.result(basketRepository.getBasketById(order.basketId), Duration.Inf)
    val orderedProducts = Await.result(orderedProductRepository.getOrderedProductByBasketId(basket.id), Duration.Inf)


    db.run {
      createPayment(id, newPayment, order, basket, orderedProducts)
    }
  }

  private val createPayment = (id: String, newPayment: Payment, orderToUpdate: Order, basketToUpdate: Basket, orderedProducts: Seq[OrderedProduct]) => {
    def createPayment = {
      payment += Payment(id, newPayment.orderId, newPayment.isDone, newPayment.date)
    }

    def updateOrderForPaymentCreation = {
      val orderToModify = Order(orderToUpdate.id, orderToUpdate.basketId, orderToUpdate.shippingInformationId, "NOT_PAID")

      order.filter(_.id === orderToModify.id).update(orderToModify)
    }

    def updateBasketForPaymentCreation = {
      val basketToModify = Basket(basketToUpdate.id, 1)

      basket.filter(_.id === basketToUpdate.id).update(basketToModify)
    }

    def updateProductsForPaymentCreation = {
      val productsToUpdate: Seq[Product] = orderedProducts
        .map(_.productId)
        .map(id => Await.result(productRepository.getProductById(id), Duration.Inf))

      val productsToModify = ListBuffer[Product]()
      for (orderedProduct <- orderedProducts) {
        val productToUpdate = productsToUpdate.filter(_.id == orderedProduct.productId).head
        if (orderedProduct.quantity > productToUpdate.quantity) {
          throw new IllegalStateException("There are not so many product with title \"" + productToUpdate.title + "\" in stock!")
        }

        productsToModify += Product(productToUpdate.id, productToUpdate.subcategoryId, productToUpdate.title, productToUpdate.price,
          productToUpdate.description, productToUpdate.date, productToUpdate.quantity - orderedProduct.quantity)
      }

      productsToModify.toList
        .map(productToModify => product.filter(_.id === productToModify.id).update(productToModify))
    }

    for {
      pay <- createPayment
      order <- updateOrderForPaymentCreation
      basket <- updateBasketForPaymentCreation
      products <- DBIO.sequence(updateProductsForPaymentCreation)
    } yield ()
    }.transactionally

  def deletePayment(paymentId: String): Future[Unit] = {
    val payment = Await.result(getPaymentById(paymentId), Duration.Inf)
    val order = Await.result(orderRepository.getOrderById(payment.orderId), Duration.Inf)
    val basket = Await.result(basketRepository.getBasketById(order.basketId), Duration.Inf)
    val orderedProducts = Await.result(orderedProductRepository.getOrderedProductByBasketId(basket.id), Duration.Inf)

    if (!order.state.equals("NOT_PAID")) {
      throw new IllegalStateException("Could not finalize payment. Order with id: " + order.id + " is not in state NOT_PAID!")
    }
    if (basket.isBought != 1) {
      throw new IllegalStateException("Could not finalize payment. Basket with id: " + basket.id + "is not bought!")
    }

    db.run {
      deletePayment(paymentId, order, basket, orderedProducts)
    }
  }

  private val deletePayment = (paymentId: String, orderToUpdate: Order, basketToDelete: Basket, orderedProducts: Seq[OrderedProduct]) => {
    def deletePayment = {
      payment.filter(_.id === paymentId).delete
    }

    def updateOrderForPaymentDeletion = {
      val orderToModify = Order(orderToUpdate.id, orderToUpdate.basketId, orderToUpdate.shippingInformationId, "CANCELLED")

      order.filter(_.id === orderToModify.id).update(orderToModify)
    }

    def updateProductsForPaymentDeletion = {
      val productsToUpdate: Seq[Product] = orderedProducts
        .map(_.productId)
        .map(id => Await.result(productRepository.getProductById(id), Duration.Inf))

      val productsToModify = ListBuffer[Product]()
      for (orderedProduct <- orderedProducts) {
        val productToUpdate = productsToUpdate.filter(_.id == orderedProduct.productId).head

        productsToModify += Product(productToUpdate.id, productToUpdate.subcategoryId, productToUpdate.title, productToUpdate.price,
          productToUpdate.description, productToUpdate.date, productToUpdate.quantity + orderedProduct.quantity)
      }

      productsToModify.toList
        .map(productToModify => product.filter(_.id === productToModify.id).update(productToModify))
    }

    for {
      pay <- deletePayment
      order <- updateOrderForPaymentDeletion
      product <- DBIO.sequence(updateProductsForPaymentDeletion)
    } yield ()
    }.transactionally

  def finalizePayment(paymentId: String): Future[Unit] = {
    val paymentToUpdate = Await.result(getPaymentById(paymentId), Duration.Inf)
    val order = Await.result(orderRepository.getOrderById(paymentToUpdate.orderId), Duration.Inf)
    val basket = Await.result(basketRepository.getBasketById(order.basketId), Duration.Inf)

    if (!order.state.equals("NOT_PAID")) {
      throw new IllegalStateException("Could not finalize payment. Order with id: " + order.id + " is not in state NOT_PAID!")
    }
    if (basket.isBought != 1) {
      throw new IllegalStateException("Could not finalize payment. Basket with id: " + basket.id + "is not bought!")
    }

    db.run {
      finalizePayment(paymentToUpdate, order)
    }
  }

  private val finalizePayment = (paymentToUpdate: Payment, orderToUpdate: Order) => {
    def updateOrderState = {
      val orderToModify = Order(orderToUpdate.id, orderToUpdate.basketId, orderToUpdate.shippingInformationId, "PAID")

      order.filter(_.id === orderToUpdate.id).update(orderToModify)
    }

    def updatePayment = {
      payment.filter(_.id === paymentToUpdate.id).update(Payment(paymentToUpdate.id, paymentToUpdate.orderId, 1, LocalDate.now()))
    }

    for {
      order <- updateOrderState
      pay <- updatePayment
    } yield ()
    }.transactionally
}
