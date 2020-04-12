package controllers.products

import javax.inject.{Inject, Singleton}
import models.products.{Opinion, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.products.{OpinionRepository, ProductRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OpinionController @Inject()(opinionRepository: OpinionRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createOpinionForm: Form[CreateOpinionForm] = Form {
    mapping(
      "productId" -> nonEmptyText,
      "opinion" -> nonEmptyText,
      "rating" -> number
    )(CreateOpinionForm.apply)(CreateOpinionForm.unapply)
  }

  def getOpinions: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    opinionRepository.getOpinions
      .map(opinions => Ok(views.html.opinions.opinions(opinions)))
  }

  def getOpinionById(opinionId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    opinionRepository.getOpinionByIdOption(opinionId)
      .map({
        case Some(opinion) => Ok(views.html.opinions.opinion(opinion))
        case None => Redirect(routes.OpinionController.getOpinions())
      })
  }

  def createOpinion: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getProducts
      .map(products => Ok(views.html.opinions.opinionadd(createOpinionForm, products)))
  }

  def createOpinionHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createOpinionForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          var products: Seq[Product] = Seq[Product]()
          productRepository.getProducts.onComplete {
            case Success(productsFromFuture) => products = productsFromFuture
            case Failure(_) => print("Failed products download")
          }

          BadRequest(views.html.opinions.opinionadd(errorForm, products))
        }
      },
      opinion => {
        opinionRepository.createOpinion(Opinion("", opinion.productId, opinion.opinion, opinion.rating))
          .map(_ => Redirect(routes.OpinionController.createOpinion()).flashing("success" -> "Opinion created!"))
      }
    )
  }

  def updateOpinion(opinionId: String) = Action {
    Ok("")
  }

  def updateOpinionHandler: Action[AnyContent] = TODO

  def deleteOpinion(opinionId: String) = Action {
    Ok("")
  }
}

case class CreateOpinionForm(
                              productId: String,
                              opinion: String,
                              rating: Int
                            )