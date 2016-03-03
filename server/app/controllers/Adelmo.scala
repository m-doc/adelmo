package controllers

import play.api.mvc.{ Action, AnyContent, Controller }

object Adelmo extends Controller {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def version: Action[AnyContent] = Action {
    Ok(org.mdoc.adelmo.BuildInfo.version)
  }
}
