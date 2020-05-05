package models.products

import java.time.LocalDate

import models.categories.SubcategoryTable
import play.api.libs.json.{Json, OFormat}
import slick.jdbc.SQLiteProfile.api._

case class Product(
                    id: String,
                    subcategoryId: String,
                    title: String,
                    price: Double,
                    description: String,
                    date: LocalDate,
                    quantity: Int
                  )

class ProductTable(tag: Tag) extends Table[Product](tag, "PRODUCT") {

  private val subcategory = TableQuery[SubcategoryTable]

  def id = column[String]("ID", O.PrimaryKey)

  def subcategoryId = column[String]("SUBCATEGORY_ID")

  def subcategoryIdFK = foreignKey("SUBCATEGORY_ID_FK", subcategoryId, subcategory)(_.id)

  def title = column[String]("TITLE")

  def price = column[Double]("PRICE")

  def description = column[String]("DESCRIPTION")

  def date = column[LocalDate]("DATE")

  def quantity = column[Int]("QUANTITY")

  def * = (id, subcategoryId, title, price, description, date, quantity) <> ((Product.apply _).tupled, Product.unapply)
}

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}