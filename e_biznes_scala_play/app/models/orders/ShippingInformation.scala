package models.orders

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class ShippingInformation(
                                id: String,
                                firstName: String,
                                lastName: String,
                                email: String,
                                street: String,
                                houseNumber: String,
                                city: String,
                                zipCode: String
                              )

class ShippingInformationTable(tag: Tag) extends Table[ShippingInformation](tag, "SHIPPING_INFORMATION") {

  val order = TableQuery[OrderTable]

  def id = column[String]("ID", O.PrimaryKey)

  def firstName = column[String]("FIRST_NAME")

  def lastName = column[String]("LAST_NAME")

  def email = column[String]("E_MAIL")

  def street = column[String]("STREET")

  def houseNumber = column[String]("HOUSE_NUMBER")

  def city = column[String]("CITY")

  def zipCode = column[String]("ZIP_CODE")

  def * = (id, firstName, lastName, email, street, houseNumber, city, zipCode) <> ((ShippingInformation.apply _).tupled, ShippingInformation.unapply)
}

object ShippingInformation {
  val shippingInformationFormat = Json.format[ShippingInformation]
}