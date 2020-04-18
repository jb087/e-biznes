package repositories.orders

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.orders.{ShippingInformation, ShippingInformationTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingInformationRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val shippingInformation = TableQuery[ShippingInformationTable]

  def getShippingInformation: Future[Seq[ShippingInformation]] = db.run {
    shippingInformation.result
  }

  def getShippingInformationById(shippingInformationId: String): Future[ShippingInformation] = db.run {
    shippingInformation.filter(_.id === shippingInformationId).result.head
  }

  def getShippingInformationByIdOption(shippingInformationId: String): Future[Option[ShippingInformation]] = db.run {
    shippingInformation.filter(_.id === shippingInformationId).result.headOption
  }

  def createShippingInformation(newShippingInformation: ShippingInformation): Future[Int] = db.run {
    val id = UUID.randomUUID().toString

    shippingInformation += ShippingInformation(id, newShippingInformation.firstName, newShippingInformation.lastName, newShippingInformation.email,
      newShippingInformation.street, newShippingInformation.houseNumber, newShippingInformation.city, newShippingInformation.zipCode)
  }

  def deleteShippingInformation(shippingInformationId: String): Future[Int] = db.run {
    shippingInformation.filter(_.id === shippingInformationId).delete
  }

  def updateShippingInformation(shippingInformationToUpdate: ShippingInformation): Future[Int] = db.run {
    shippingInformation.filter(_.id === shippingInformationToUpdate.id).update(shippingInformationToUpdate)
  }
}
