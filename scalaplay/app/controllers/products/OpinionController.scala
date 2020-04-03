package controllers.products

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class OpinionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getOpinions = Action {
    Ok("")
  }

  def createOpinion = Action {
    Ok("")
  }

  def updateOpinion(opinionId: String) = Action {
    Ok("")
  }

  def deleteOpinion(opinionId: String) = Action {
    Ok("")
  }
}
