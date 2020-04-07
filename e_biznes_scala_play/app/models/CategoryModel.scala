package models

import slick.jdbc.SQLiteProfile.api._

object CategoryModel {

  case class Category(
                       id: String,
                       name: String
                     )

  class CategoryTable(tag: Tag) extends Table[Category](tag, "CATEGORY") {

    def id = column[String]("ID", O.PrimaryKey)
    def name = column[String]("NAME", O.Unique)
    override def * = (id, name) <>(Category.tupled, Category.unapply)
  }
}
