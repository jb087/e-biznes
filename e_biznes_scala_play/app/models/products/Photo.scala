package models.products

import play.api.libs.json.{Json, OFormat}
import slick.jdbc.SQLiteProfile.api._

case class Photo(
                  id: String,
                  productId: String,
                  photo: Array[Byte]
                )

class PhotoTable(tag: Tag) extends Table[Photo](tag, "PHOTO") {

  val product = TableQuery[ProductTable]

  def id = column[String]("ID", O.PrimaryKey)

  def productId = column[String]("PRODUCT_ID")

  def productIdFK = foreignKey("PRODUCT_ID_FK", productId, product)(_.id)

  def photo = column[Array[Byte]]("PHOTO")

  def * = (id, productId, photo)  <> ((Photo.apply _).tupled, Photo.unapply)

}

object Photo {
  implicit val photoFormat: OFormat[Photo] = Json.format[Photo]
}
