package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object Adelmo extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def version = Action {
    Ok(org.mdoc.adelmo.BuildInfo.version)
  }
}
