package repositories.basket

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.basket.{Basket, BasketTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val basket = TableQuery[BasketTable]

  def getBaskets: Future[Seq[Basket]] = db.run {
    basket.result
  }

  def getBasketsByUserId(userId: String): Future[Seq[Basket]] = db.run {
    basket.filter(_.isBought === 1).filter(_.userId === userId).result
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

  def createBasket(userId: String, isBought: Int): Future[String] = {
    val id: String = UUID.randomUUID().toString
    db.run {
      basket += Basket(id, userId, isBought)
    }.map(_ => id)
  }

  def createBasket(isBought: Int): Future[String] = {
    val id: String = UUID.randomUUID().toString
    db.run {
      basket += Basket(id, "", isBought)
    }.map(_ => id)
  }

  def deleteBasket(basketId: String): Future[Int] = db.run {
    basket.filter(_.id === basketId).filter(_.isBought === 0).delete
  }

  def updateBasket(basketToUpdate: Basket): Future[Int] = db.run {
    basket.filter(_.id === basketToUpdate.id).filter(_.isBought === 0).update(basketToUpdate)
  }
}
