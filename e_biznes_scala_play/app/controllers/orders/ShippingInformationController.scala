package controllers.orders

import javax.inject.{Inject, Singleton}
import models.orders.ShippingInformation
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.orders.ShippingInformationRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingInformationController @Inject()(shippingInformationRepository: ShippingInformationRepository, cc: MessagesControllerComponents)
                                             (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createShippingInformationForm: Form[CreateShippingInformationForm] = Form {
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> email,
      "street" -> nonEmptyText,
      "houseNumber" -> nonEmptyText,
      "city" -> nonEmptyText,
      "zipCode" -> nonEmptyText
    )(CreateShippingInformationForm.apply)(CreateShippingInformationForm.unapply)
  }

  val updateShippingInformationForm: Form[UpdateShippingInformationForm] = Form {
    mapping(
      "shippingInformationId" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> email,
      "street" -> nonEmptyText,
      "houseNumber" -> nonEmptyText,
      "city" -> nonEmptyText,
      "zipCode" -> nonEmptyText
    )(UpdateShippingInformationForm.apply)(UpdateShippingInformationForm.unapply)
  }

  def getShippingInformation: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.getShippingInformation
      .map(shippingInformation => Ok(views.html.shippingInformation.shippinginformation(shippingInformation)))
  }

  def getShippingInformationById(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.getShippingInformationByIdOption(shippingInformationId)
      .map({
        case Some(shippingInformation) => Ok(views.html.shippingInformation.information(shippingInformation))
        case None => Redirect(routes.ShippingInformationController.getShippingInformation())
      })
  }

  def createShippingInformation: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.shippingInformation.shippinginformationadd(createShippingInformationForm))
  }

  def createShippingInformationHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createShippingInformationForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          BadRequest(views.html.shippingInformation.shippinginformationadd(errorForm))
        }
      },
      information => {
        shippingInformationRepository.createShippingInformation(
          ShippingInformation("", information.firstName, information.lastName,
            information.email, information.street, information.houseNumber, information.city, information.zipCode)
        )
          .map(_ => Redirect(routes.ShippingInformationController.createShippingInformation()).flashing("success" -> "Shipping Information Created!")
          )
      }
    )
  }

  def updateShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.getShippingInformationById(shippingInformationId)
      .map(information => {
        val shippingInformationForm = updateShippingInformationForm.fill(
          UpdateShippingInformationForm(information.id, information.firstName, information.lastName,
            information.email, information.street, information.houseNumber, information.city, information.zipCode)
        )

        Ok(views.html.shippingInformation.shippinginformationupdate(shippingInformationForm))
      })
  }

  def updateShippingInformationHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateShippingInformationForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          BadRequest(views.html.shippingInformation.shippinginformationupdate(errorForm))
        }
      },
      information => {
        shippingInformationRepository.updateShippingInformation(
          ShippingInformation(information.shippingInformationId, information.firstName, information.lastName,
            information.email, information.street, information.houseNumber, information.city, information.zipCode)
        )
          .map(_ => Redirect(routes.ShippingInformationController.updateShippingInformation(information.shippingInformationId))
            .flashing("success" -> "Shipping Information Updated!")
          )
      }
    )
  }

  def deleteShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    shippingInformationRepository.deleteShippingInformation(shippingInformationId)
      .map(_ => Redirect(routes.ShippingInformationController.getShippingInformation()))
  }
}

case class CreateShippingInformationForm(
                                          firstName: String,
                                          lastName: String,
                                          email: String,
                                          street: String,
                                          houseNumber: String,
                                          city: String,
                                          zipCode: String
                                        )

case class UpdateShippingInformationForm(
                                          shippingInformationId: String,
                                          firstName: String,
                                          lastName: String,
                                          email: String,
                                          street: String,
                                          houseNumber: String,
                                          city: String,
                                          zipCode: String
                                        )