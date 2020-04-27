package repositories.orders

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.orders.{Order, OrderTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val order = TableQuery[OrderTable]

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

    order += Order(id, newOrder.basketId, newOrder.shippingInformationId, "CANCELLED")
  }

  def deleteOrder(orderId: String): Future[Int] = db.run {
    val orderFromDB = Await.result(getOrderById(orderId), Duration.Inf)
    if (!orderFromDB.state.equals("CANCELLED")) {
      throw new IllegalStateException("Could not delete order in state PAID or DELIVERED!")
    }

    order.filter(_.id === orderId).delete
  }

  def updateOrder(orderToUpdate: Order): Future[Int] = db.run {
    val orderFromDB = Await.result(getOrderById(orderToUpdate.id), Duration.Inf)
    if (orderFromDB.state.equals("PAID") || orderFromDB.state.equals("DELIVERED")) {
      throw new IllegalStateException("Could not update order in state PAID or DELIVERED!")
    }

    val orderToModify = Order(orderToUpdate.id, orderToUpdate.basketId, orderToUpdate.shippingInformationId, orderFromDB.state)

    order.filter(_.id === orderToModify.id).update(orderToModify)
  }

  def deliverOrder(orderId: String): Future[Int] = db.run {
    val orderFromDB = Await.result(getOrderById(orderId), Duration.Inf)
    if (!orderFromDB.state.equals("PAID")) {
      throw new IllegalStateException("Could not deliver order in other than PAID!")
    }

    val orderToDeliver = Order(orderId, orderFromDB.basketId, orderFromDB.shippingInformationId, "DELIVERED")

    order.filter(_.id === orderId).update(orderToDeliver)
  }
}
