package repositories

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Subcategory, SubcategoryTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubcategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val subcategory = TableQuery[SubcategoryTable]

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

  def updateSubcategory(subcategoryToUpdate: Subcategory): Future[Int] = db.run {
    subcategory.filter(_.id === subcategoryToUpdate.id).update(subcategoryToUpdate)
  }

  def deleteSubcategory(subcategoryId: String): Future[Int] = db.run {
    subcategory.filter(_.id === subcategoryId).delete
  }
}
