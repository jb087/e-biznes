package repositories.basket

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.basket.{OrderedProduct, OrderedProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderedProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val orderedProduct = TableQuery[OrderedProductTable]

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
    orderedProduct.filter(_.id === orderedProductId).delete
  }

  def updateOrderedProduct(orderedProductToUpdate: OrderedProduct): Future[Int] = db.run {
    orderedProduct.filter(_.id === orderedProductToUpdate.id).update(orderedProductToUpdate)
  }
}
