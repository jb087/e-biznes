package models.basket

import models.products.ProductTable
import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class OrderedProduct(
                           id: String,
                           basketId: String,
                           productId: String,
                           quantity: Int
                         )

class OrderedProductTable(tag: Tag) extends Table[OrderedProduct](tag, "ORDERED_PRODUCT") {

  val basket = TableQuery[BasketTable]
  val product = TableQuery[ProductTable]

  def id = column[String]("ID", O.PrimaryKey)

  def basketId = column[String]("BASKET_ID")

  def basketId_fk = foreignKey("BASKET_ID_FK", basketId, basket)(_.id)

  def productId = column[String]("PRODUCT_ID")

  def productId_fk = foreignKey("PRODUCT_ID_FK", productId, product)(_.id)

  def quantity = column[Int]("QUANTITY")

  def * = (id, basketId, productId, quantity) <> ((OrderedProduct.apply _).tupled, OrderedProduct.unapply)
}

object OrderedProduct {
  val orderedProductFormat = Json.format[OrderedProduct]
}