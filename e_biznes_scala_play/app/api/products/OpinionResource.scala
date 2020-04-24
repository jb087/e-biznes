package api.products

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.products.{OpinionRepository, ProductRepository}

import scala.concurrent.ExecutionContext

@Singleton
class OpinionResource @Inject()(opinionRepository: OpinionRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getOpinions: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def getOpinionById(opinionId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def createOpinion: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def updateOpinion(opinionId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }

  def deleteOpinion(opinionId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

  }
}