package repositories.orders

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.orders.{Order, OrderTable, Payment, PaymentTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(orderRepository: OrderRepository, dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val payment = TableQuery[PaymentTable]
  val order = TableQuery[OrderTable]

  def getPayments: Future[Seq[Payment]] = db.run {
    payment.result
  }

  def getPaymentById(paymentId: String): Future[Payment] = db.run {
    payment.filter(_.id === paymentId).result.head
  }

  def getPaymentByIdOption(paymentId: String): Future[Option[Payment]] = db.run {
    payment.filter(_.id === paymentId).result.headOption
  }

  def createPayment(newPayment: Payment): Future[Unit] = {
    val id = UUID.randomUUID().toString
    val order = Await.result(orderRepository.getOrderById(newPayment.orderId), Duration.Inf)

    db.run {
      createPayment(id, newPayment, Order(order.id, order.basketId, order.shippingInformationId, "NOT_PAID"))
    }
  }

  private val createPayment = (id: String, newPayment: Payment, orderToUpdate: Order) => {
    for {
      pay <- payment += Payment(id, newPayment.orderId, newPayment.isDone, newPayment.date)
      order <- order.filter(_.id === orderToUpdate.id).update(orderToUpdate)
    } yield ()
  }.transactionally
}
