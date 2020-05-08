package repositories.products

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.products.{Opinion, OpinionTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OpinionRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val opinion = TableQuery[OpinionTable]

  def getOpinions: Future[Seq[Opinion]] = db.run {
    opinion.result
  }

  def getOpinionsByProductId(productId: String): Future[Seq[Opinion]] = db.run {
    opinion.filter(_.productId === productId).result
  }

  def getOpinionById(opinionId: String): Future[Opinion] = db.run {
    opinion.filter(_.id === opinionId).result.head
  }

  def getOpinionByIdOption(opinionId: String): Future[Option[Opinion]] = db.run {
    opinion.filter(_.id === opinionId).result.headOption
  }

  def createOpinion(newOpinion: Opinion): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    opinion += Opinion(id, newOpinion.productId, newOpinion.opinion, newOpinion.rating)
  }

  def deleteOpinion(opinionId: String): Future[Int] = db.run {
    opinion.filter(_.id === opinionId).delete
  }

  def updateOpinion(opinionToUpdate: Opinion): Future[Int] = db.run {
    opinion.filter(_.id === opinionToUpdate.id).update(opinionToUpdate)
  }
}
