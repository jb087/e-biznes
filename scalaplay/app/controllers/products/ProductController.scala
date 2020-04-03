package controllers.products

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getProducts = Action {
    Ok("")
  }

  def createProduct = Action {
    Ok("")
  }

  def updateProduct(productId: String) = Action {
    Ok("")
  }

  def deleteProduct(productId: String) = Action {
    Ok("")
  }
}
