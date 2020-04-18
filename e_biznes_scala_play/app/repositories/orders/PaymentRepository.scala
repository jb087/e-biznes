package repositories.orders

import javax.inject.{Inject, Singleton}
import models.orders.{Payment, PaymentTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val payment = TableQuery[PaymentTable]

  def getPayments: Future[Seq[Payment]] = db.run {
    payment.result
  }

  def getPaymentById(paymentId: String): Future[Payment] = db.run {
    payment.filter(_.id === paymentId).result.head
  }

  def getPaymentByIdOption(paymentId: String): Future[Option[Payment]] = db.run {
    payment.filter(_.id === paymentId).result.headOption
  }
}
