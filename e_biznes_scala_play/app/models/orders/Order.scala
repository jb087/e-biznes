package models.orders

import models.basket.BasketTable
import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Order(
                  id: String,
                  basketId: String,
                  state: String
                )

class OrderTable(tag: Tag) extends Table[Order](tag, "ORDER") {

  val basket = TableQuery[BasketTable]

  def id = column[String]("ID", O.PrimaryKey)

  def basketId = column[String]("BASKET_ID", O.Unique)

  def basketId_fk = foreignKey("BASKET_ID_FK", basketId, basket)(_.id)

  def state = column[String]("STATE")

  def * = (id, basketId, state) <> ((Order.apply _).tupled, Order.unapply)
}

object Order {
  val orderFormat = Json.format[Order]
}