package repositories.orders

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.orders.{Order, OrderTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val order = TableQuery[OrderTable]

  def getOrders: Future[Seq[Order]] = db.run {
    order.result
  }

  def getOrderById(orderId: String): Future[Order] = db.run {
    order.filter(_.id === orderId).result.head
  }

  def getOrderByIdOption(orderId: String): Future[Option[Order]] = db.run {
    order.filter(_.id === orderId).result.headOption
  }

  def createOrder(newOrder: Order): Future[Int] = db.run {
    val id = UUID.randomUUID().toString

    order += Order(id, newOrder.basketId, newOrder.shippingInformationId, newOrder.state)
  }

  def deleteOrder(orderId: String): Future[Int] = db.run {
    order.filter(_.id === orderId).delete
  }

  def updateOrder(orderToUpdate: Order): Future[Int] = db.run {
    order.filter(_.id === orderToUpdate.id).update(orderToUpdate)
  }
}
