package repositories.products

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.products.{Photo, PhotoTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PhotoRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val photo = TableQuery[PhotoTable]

  def getPhotos: Future[Seq[Photo]] = db.run {
    photo.result
  }

  def getPhotoById(photoId: String): Future[Photo] = db.run {
    photo.filter(_.id === photoId).result.head
  }

  def getPhotoByIdOption(photoId: String): Future[Option[Photo]] = db.run {
    photo.filter(_.id === photoId).result.headOption
  }

  def createPhoto(newPhoto: Photo): Future[Int] = db.run {
    val id: String = UUID.randomUUID().toString

    photo += Photo(id, newPhoto.productId, newPhoto.photo)
  }
}
