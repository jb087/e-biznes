package models.orders

import models.basket.BasketTable
import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Order(
                  id: String,
                  basketId: String,
                  shippingInformationId: String,
                  state: String
                )

class OrderTable(tag: Tag) extends Table[Order](tag, "ORDER") {

  val basket = TableQuery[BasketTable]
  val shippingInformation = TableQuery[ShippingInformationTable]

  def id = column[String]("ID", O.PrimaryKey)

  def basketId = column[String]("BASKET_ID", O.Unique)

  def basketId_fk = foreignKey("BASKET_ID_FK", basketId, basket)(_.id)

  def shippingInformationId = column[String]("SHIPPING_INFORMATION_ID")

  def shippingInformationId_fk = foreignKey("SHIPPING_INFORMATION_ID_FK", shippingInformationId, shippingInformation)(_.id)

  def state = column[String]("STATE")

  def * = (id, basketId, shippingInformationId, state) <> ((Order.apply _).tupled, Order.unapply)
}

object Order {
  implicit val orderFormat = Json.format[Order]
}