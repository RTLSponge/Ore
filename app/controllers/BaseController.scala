package controllers

import controllers.sugar.{Actions, Bakery}
import db.ModelService
import db.access.ModelAccess
import db.impl.VersionTable
import db.impl.access.{OrganizationBase, ProjectBase, UserBase}
import models.project.{Project, Version}
import models.user.SignOn
import ore.{OreConfig, OreEnv}
import play.api.i18n.I18nSupport
import play.api.mvc._
import security.spauth.SingleSignOnConsumer
import util.StringUtils._

/**
  * Represents a Secured base Controller for this application.
  */
abstract class BaseController(implicit val env: OreEnv,
                              val config: OreConfig,
                              val service: ModelService,
                              override val bakery: Bakery,
                              override val sso: SingleSignOnConsumer)
                              extends Controller
                                with Actions
                                with I18nSupport {

  implicit override val users: UserBase = this.service.getModelBase(classOf[UserBase])
  implicit override val projects: ProjectBase = this.service.getModelBase(classOf[ProjectBase])
  implicit override val organizations: OrganizationBase = this.service.getModelBase(classOf[OrganizationBase])

  override val signOns: ModelAccess[SignOn] = this.service.access[SignOn](classOf[SignOn])

  override def notFound()(implicit request: Request[_]) = NotFound(views.html.errors.notFound())

  /**
    * Executes the given function with the specified result or returns a
    * NotFound if not found.
    *
    * @param author   Project author
    * @param slug     Project slug
    * @param fn       Function to execute
    * @param request  Incoming request
    * @return         NotFound or function result
    */
  def withProject(author: String, slug: String)(fn: Project => Result)(implicit request: Request[_]): Result
  = this.projects.withSlug(author, slug).map(fn).getOrElse(notFound)

  /**
    * Executes the given function with the specified result or returns a
    * NotFound if not found.
    *
    * @param versionString  VersionString
    * @param fn             Function to execute
    * @param request        Incoming request
    * @param project        Project to get version from
    * @return               NotFound or function result
    */
  def withVersion(versionString: String)(fn: Version => Result)
                 (implicit request: Request[_], project: Project): Result
  = project.versions.find(equalsIgnoreCase[VersionTable](_.versionString, versionString)).map(fn).getOrElse(notFound)

}
