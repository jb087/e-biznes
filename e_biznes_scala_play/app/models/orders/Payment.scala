package models.orders

import java.time.LocalDate

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Payment(
                    id: String,
                    orderId: String,
                    isDone: Int,
                    date: LocalDate
                  )

class PaymentTable(tag: Tag) extends Table[Payment](tag, "PAYMENT") {

  val order = TableQuery[OrderTable]

  def id = column[String]("ID", O.PrimaryKey)

  def orderId = column[String]("ORDER_ID", O.Unique)

  def orderId_fk = foreignKey("ORDER_ID_FK", orderId, order)(_.id)

  def isDone = column[Int]("IS_DONE")

  def date = column[LocalDate]("DATE")

  def * = (id, orderId, isDone, date) <> ((Payment.apply _).tupled, Payment.unapply)
}

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}