package repositories

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
}
