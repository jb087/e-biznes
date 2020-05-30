package repositories.products

import java.time.LocalDate
import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.products.{Product, ProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

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

    product += Product(id, newProduct.subcategoryId, newProduct.title, newProduct.price, newProduct.description, LocalDate.now(), newProduct.quantity)
  }

  def deleteProduct(productId: String): Future[Int] = db.run {
    product.filter(_.id === productId).delete
  }

  def updateProduct(productToUpdate: Product): Future[Int] = db.run {
    val productToModify = Product(productToUpdate.id, productToUpdate.subcategoryId, productToUpdate.title, productToUpdate.price,
      productToUpdate.description, LocalDate.now(), productToUpdate.quantity)

    product.filter(_.id === productToModify.id).update(productToModify)
  }
}
