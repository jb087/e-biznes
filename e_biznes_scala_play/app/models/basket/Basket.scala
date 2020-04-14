package models.basket

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Basket(
                   id: String,
                   isBought: Int
                 )

class BasketTable(tag: Tag) extends Table[Basket](tag, "BASKET") {

  def id = column[String]("ID", O.PrimaryKey)

  def isBought = column[Int]("IS_BOUGHT")

  def * = (id, isBought) <> ((Basket.apply _).tupled, Basket.unapply)
}

object Basket {
  val basketFormat = Json.format[Basket]
}