package repositories.basket

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.basket.{Basket, BasketTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val basket = TableQuery[BasketTable]

  def getBaskets: Future[Seq[Basket]] = db.run {
    basket.result
  }

  def getBaskets(isBought: Int): Future[Seq[Basket]] = db.run {
    basket.filter(_.isBought === isBought).result
  }

  def getBasketById(basketId: String): Future[Basket] = db.run {
    basket.filter(_.id === basketId).result.head
  }

  def getBasketByIdOption(basketId: String): Future[Option[Basket]] = db.run {
    basket.filter(_.id === basketId).result.headOption
  }

  def createBasket(isBought: Int): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    basket += Basket(id, isBought)
  }

  def deleteBasket(basketId: String): Future[Int] = db.run {
    basket.filter(_.id === basketId).delete
  }

  def updateBasket(basketToUpdate: Basket): Future[Int] = db.run {
    basket.filter(_.id === basketToUpdate.id).update(basketToUpdate)
  }
}
