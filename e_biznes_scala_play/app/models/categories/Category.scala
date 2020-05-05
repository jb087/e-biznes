package models.categories

import play.api.libs.json.{Json, OFormat}
import slick.jdbc.SQLiteProfile.api._

case class Category(
                     id: String,
                     name: String
                   )

class CategoryTable(tag: Tag) extends Table[Category](tag, "CATEGORY") {

  def id = column[String]("ID", O.PrimaryKey)

  def name = column[String]("NAME", O.Unique)

  override def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
}

object Category {
  implicit val categoryFormat: OFormat[Category] = Json.format[Category]
}