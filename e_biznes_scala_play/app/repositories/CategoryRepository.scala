package repositories

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Category, CategoryTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val category = TableQuery[CategoryTable]

  def getCategories: Future[Seq[Category]] = db.run {
    category.result
  }

  def getCategoryById(categoryId: String): Future[Category] = db.run {
    category.filter(_.id === categoryId).result.head
  }

  def createCategory(name: String): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    category += Category(id, name)
  }

  def deleteCategory(categoryId: String): Future[Int] = db.run {
    category.filter(_.id === categoryId).delete
  }

  def updateCategory(categoryToUpdate: Category): Future[Int] = db.run {
    category.filter(_.id === categoryToUpdate.id).update(categoryToUpdate)
  }
}
