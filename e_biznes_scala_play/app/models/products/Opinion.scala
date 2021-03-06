package models.products

import play.api.libs.json.{Json, OFormat}
import slick.jdbc.SQLiteProfile.api._

case class Opinion(
                    id: String,
                    productId: String,
                    opinion: String,
                    rating: Int
                  )

class OpinionTable(tag: Tag) extends Table[Opinion](tag, "OPINION") {

  val product = TableQuery[ProductTable]

  def id = column[String]("ID", O.PrimaryKey)

  def productId = column[String]("PRODUCT_ID")

  def productIdFK = foreignKey("PRODUCT_ID_FK", productId, product)(_.id)

  def opinion = column[String]("OPINION")

  def rating = column[Int]("RATING")

  def * = (id, productId, opinion, rating) <> ((Opinion.apply _).tupled, Opinion.unapply)
}

object Opinion {
  implicit val opinionFormat: OFormat[Opinion] = Json.format[Opinion]
}
