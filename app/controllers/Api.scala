package controllers

import play.api.Play.current
import play.api.libs.ws.WS
import play.api.mvc.{ Action, AnyContent, Controller }

import scala.concurrent.ExecutionContext.Implicits.global

object Api extends Controller {

  def templates: Action[AnyContent] = Action.async {
    val url = current.configuration
      .getString("adelmo.template-service.url")
      .fold("")(_ + "/api/template-views")
    WS.url(url).get().map(response => Ok(response.body))
  }
}
