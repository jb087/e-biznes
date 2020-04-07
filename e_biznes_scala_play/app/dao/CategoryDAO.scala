package dao

import javax.inject.Inject
import models.CategoryModel.{Category, CategoryTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class CategoryDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  private val Categories = TableQuery[CategoryTable]

  def all(): Future[Seq[Category]] = db.run(Categories.result)
}
