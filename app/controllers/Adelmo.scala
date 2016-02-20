package controllers

import play.api.mvc.{ Action, Controller }

object Adelmo extends Controller {
  def index = Action {
    Ok(views.html.adelmo())
  }

  def version = Action {
    Ok(org.mdoc.adelmo.BuildInfo.version)
  }
}
