package repositories.categories

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.categories.{Subcategory, SubcategoryTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class SubcategoryRepository @Inject()(categoryRepository: CategoryRepository, dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val subcategory = TableQuery[SubcategoryTable]

  def getSubcategories: Future[Seq[Subcategory]] = db.run {
    subcategory.result
  }

  def getSubcategoryById(subcategoryId: String): Future[Subcategory] = db.run {
    subcategory.filter(_.id === subcategoryId).result.head
  }

  def getSubcategoryByIdOption(subcategoryId: String): Future[Option[Subcategory]] = db.run {
    subcategory.filter(_.id === subcategoryId).result.headOption
  }

  def createSubcategory(parentId: String, name: String): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    subcategory += Subcategory(id, parentId, name)
  }

  def updateSubcategory(subcategoryToUpdate: Subcategory): Future[Int] = {
    Await.result(categoryRepository.getCategoryByIdOption(subcategoryToUpdate.parentId)
      .map({
        case Some(value) => value
        case None => throw new IllegalArgumentException("Provided parentId does not exist in Category table!")
      }), Duration.Inf)

    db.run {
      subcategory.filter(_.id === subcategoryToUpdate.id).update(subcategoryToUpdate)
    }
  }

  def deleteSubcategory(subcategoryId: String): Future[Int] = db.run {
    subcategory.filter(_.id === subcategoryId).delete
  }
}
