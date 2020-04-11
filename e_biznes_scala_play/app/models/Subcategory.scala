package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Subcategory(
                        id: String,
                        parentId: String,
                        name: String
                      )

class SubcategoryTable(tag: Tag) extends Table[Subcategory](tag, "SUBCATEGORY") {

  private val category = TableQuery[CategoryTable]

  def id = column[String]("ID", O.PrimaryKey)

  def parentId = column[String]("PARENT_ID")

  def parentId_fk = foreignKey("PARENT_ID_FK", parentId, category)(_.id)

  def name = column[String]("NAME", O.Unique)

  override def * = (id, parentId, name) <> ((Subcategory.apply _).tupled, Subcategory.unapply)
}

object Subcategory {
  implicit val subcategoryFormat = Json.format[Subcategory]
}