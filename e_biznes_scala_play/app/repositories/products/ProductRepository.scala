package repositories.products

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.products.{Product, ProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val product = TableQuery[ProductTable]

  def getProducts: Future[Seq[Product]] = db.run {
    product.result
  }

  def getProductById(productId: String): Future[Product] = db.run {
    product.filter(_.id === productId).result.head
  }

  def getProductByIdOption(productId: String): Future[Option[Product]] = db.run {
    product.filter(_.id === productId).result.headOption
  }

  def createProduct(newProduct: Product): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    product += Product(id, newProduct.subcategoryId, newProduct.title, newProduct.price, newProduct.description, newProduct.date, newProduct.quantity)
  }

  def deleteProduct(productId: String): Future[Int] = db.run {
    product.filter(_.id === productId).delete
  }

  def updateProduct(productToUpdate: Product): Future[Int] = db.run {
    product.filter(_.id === productToUpdate.id).update(productToUpdate)
  }

  def decreaseProductQuantity(productId: String, subtractQuantity: Int) = {
      getProductById(productId)
        .onComplete( {
          case Success(productFromFuture) =>
            val productToUpdate = Product(productFromFuture.id, productFromFuture.subcategoryId, productFromFuture.title, productFromFuture.price,
              productFromFuture.description, productFromFuture.date, productFromFuture.quantity - subtractQuantity)
            updateProduct(productToUpdate)
          case Failure(_) => print("Failed product quantity decrease")
        })
  }
}
