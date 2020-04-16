package controllers.orders

import javax.inject.{Inject, Singleton}
import models.orders.ShippingInformation
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repositories.orders.{OrderRepository, ShippingInformationRepository}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ShippingInformationController @Inject()(shippingInformationRepository: ShippingInformationRepository, orderRepository: OrderRepository,
                                              cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createShippingInformationForm: Form[CreateShippingInformationForm] = Form {
    mapping(
      "orderId" -> nonEmptyText,
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
      "orderId" -> nonEmptyText,
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

  def createShippingInformation: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getOrders
      .map(orders => Ok(views.html.shippingInformation.shippinginformationadd(createShippingInformationForm, orders)))
  }

  def createShippingInformationHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    createShippingInformationForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val orders = Await.result(orderRepository.getOrders, Duration.Inf)

          BadRequest(views.html.shippingInformation.shippinginformationadd(errorForm, orders))
        }
      },
      information => {
        shippingInformationRepository.createShippingInformation(
          ShippingInformation("", information.orderId, information.firstName, information.lastName,
          information.email, information.street, information.houseNumber, information.city, information.zipCode)
        )
          .map(_ => Redirect(routes.ShippingInformationController.createShippingInformation()).flashing("success" -> "Shipping Information Created!")
        )
      }
    )
  }

  def updateShippingInformation(shippingInformationId: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val orders = Await.result(orderRepository.getOrders, Duration.Inf)

    shippingInformationRepository.getShippingInformationById(shippingInformationId)
      .map(information => {
        val shippingInformationForm = updateShippingInformationForm.fill(
          UpdateShippingInformationForm(information.id, information.orderId, information.firstName, information.lastName,
            information.email, information.street, information.houseNumber, information.city, information.zipCode)
        )

        Ok(views.html.shippingInformation.shippinginformationupdate(shippingInformationForm, orders))
      })
  }

  def updateShippingInformationHandler: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateShippingInformationForm.bindFromRequest().fold(
      errorForm => {
        Future.successful {
          val orders = Await.result(orderRepository.getOrders, Duration.Inf)

          BadRequest(views.html.shippingInformation.shippinginformationupdate(errorForm, orders))
        }
      },
      information => {
        shippingInformationRepository.updateShippingInformation(
          ShippingInformation(information.shippingInformationId, information.orderId, information.firstName, information.lastName,
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
                                          orderId: String,
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
                                          orderId: String,
                                          firstName: String,
                                          lastName: String,
                                          email: String,
                                          street: String,
                                          houseNumber: String,
                                          city: String,
                                          zipCode: String
                                        )