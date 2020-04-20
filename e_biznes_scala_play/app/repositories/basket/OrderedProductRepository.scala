package repositories.basket

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.basket.{BasketTable, OrderedProduct, OrderedProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderedProductRepository @Inject()(basketRepository: BasketRepository, dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val orderedProduct = TableQuery[OrderedProductTable]

  def getOrderedProducts: Future[Seq[OrderedProduct]] = db.run {
    orderedProduct.result
  }

  def getOrderedProductById(orderedProductId: String): Future[OrderedProduct] = db.run {
    orderedProduct.filter(_.id === orderedProductId).result.head
  }

  def getOrderedProductByIdOption(orderedProductId: String): Future[Option[OrderedProduct]] = db.run {
    orderedProduct.filter(_.id === orderedProductId).result.headOption
  }

  def getOrderedProductByBasketId(basketId: String): Future[Seq[OrderedProduct]] = db.run {
    orderedProduct.filter(_.basketId === basketId).result
  }

  def createOrderedProduct(newOrderedProduct: OrderedProduct): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    orderedProduct += OrderedProduct(id, newOrderedProduct.basketId, newOrderedProduct.productId, newOrderedProduct.quantity)
  }

  def deleteOrderedProduct(orderedProductId: String): Future[Int] = db.run {
    val product = Await.result(getOrderedProductById(orderedProductId), Duration.Inf)
    val basket = Await.result(basketRepository.getBasketById(product.basketId), Duration.Inf)
    if (basket.isBought != 0) {
      throw new IllegalStateException("Cannot delete ordered product associated with bought basket!")
    }

    orderedProduct.filter(_.id === orderedProductId).delete
  }

  def updateOrderedProduct(orderedProductToUpdate: OrderedProduct): Future[Int] = db.run {
    val basket = Await.result(basketRepository.getBasketById(orderedProductToUpdate.basketId), Duration.Inf)
    if (basket.isBought != 0) {
      throw new IllegalStateException("Cannot update ordered product associated with bought basket!")
    }

    orderedProduct.filter(_.id === orderedProductToUpdate.id).update(orderedProductToUpdate)
  }
}
