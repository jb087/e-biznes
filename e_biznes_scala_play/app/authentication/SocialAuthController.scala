package authentication

import java.util.UUID

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, HTTPLayer}
import com.mohiva.play.silhouette.api.{Logger, LoginEvent, Silhouette}
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.providers.oauth2.{GitHubProvider, GoogleProvider}
import javax.inject.Inject
import models.auth.services._
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class SocialAuthController @Inject()(components: ControllerComponents,
                                     silhouette: Silhouette[DefaultEnv],
                                     configuration: Configuration,
                                     clock: Clock,
                                     userService: UserService,
                                     authenticateService: AuthenticateService,
                                     authInfoRepository: AuthInfoRepository,
                                     socialProviderRegistry: SocialProviderRegistry,
                                     httpLayer: HTTPLayer,
                                     socialStateHandler: SocialStateHandler)
                                    (implicit ex: ExecutionContext) extends AbstractAuthController(silhouette, configuration, clock) with Logger {

  def authenticate(provider: String) = Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            userBindResult <- authenticateService.provideUserForSocialAccount(provider, profile, authInfo)
            result <- userBindResult match {
              case AccountBound(u) =>
                authenticateUser(u, profile.loginInfo, rememberMe = true)
              case EmailIsBeingUsed(providers) =>
                Future.successful(Conflict(Json.obj("error" -> "EmailIsBeingUsed", "providers" -> providers)))
              case NoEmailProvided =>
                Future.successful(BadRequest(Json.obj("error" -> "NoEmailProvided")))
            }
          } yield result
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Redirect("/error?message=socialAuthFailed")
    }
  }

  def authenticateAdminPanel(provider: String) = Action.async { implicit request =>

    var socialProvider: Option[SocialProvider with CommonSocialProfileBuilder] = None
    provider match {
      case "github" => socialProvider = Option(new GitHubProvider(httpLayer, socialStateHandler, configuration.underlying.as[OAuth2Settings]("silhouette.admin.panel.github")))
      case "google" => socialProvider = Option(new GoogleProvider(httpLayer, socialStateHandler, configuration.underlying.as[OAuth2Settings]("silhouette.admin.panel.google")))
    }

    (socialProvider match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            user <- userService.createOrUpdate(
              profile.loginInfo,
              profile.email match {
                case Some(email) => email
                case None => throw new IllegalStateException("Email not provided!")
              },
              profile.firstName,
              profile.lastName,
              profile.avatarURL
            )
            saveAuth <- authenticateService.addAuthenticateMethod(UUID.fromString(user.userID), profile.loginInfo, authInfo)
            authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
            result <- authenticateAdminPanelUser(user, profile.loginInfo, rememberMe = true)
          } yield {
            silhouette.env.eventBus.publish(LoginEvent(user, request))
            result
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Redirect(controllers.routes.HomeController.signIn()).flashing("error" -> "Could not authenticate!")
    }
  }
}
