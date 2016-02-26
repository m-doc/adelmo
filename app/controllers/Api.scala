package controllers

import io.circe.generic.auto._
import io.circe.syntax._
import org.mdoc.common.model._
import org.mdoc.common.model.circe._
import org.mdoc.common.model.Format.{ Html, Pdf }
import org.mdoc.common.model.RenderingEngine.LibreOffice
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.{ WS, WSResponse }
import play.api.mvc.{ Action, AnyContent, Controller, Result }
import scala.concurrent.ExecutionContext.Implicits.global
import scodec.bits.ByteVector

object Api extends Controller {

  def templates: Action[AnyContent] = Action.async {
    val url = current.configuration
      .getString("adelmo.template-service.url")
      .fold("")(_ + "/api/template-views")
    WS.url(url).get().map(toResult)
  }

  def placeholders(name: String): Action[AnyContent] = Action.async {
    val url = current.configuration
      .getString("adelmo.template-service.url")
      .fold("")(_ + s"/api/template-placeholders/$name")
    WS.url(url).get().map(toResult)
  }

  def renderTemplate(name: String): Action[AnyContent] = Action.async { req =>
    println(req)
    println(req.body)

    val tempUrl = current.configuration
      .getString("adelmo.template-service.url")
      .fold("")(_ + s"/api/process/$name")
    val rendUrl = current.configuration
      .getString("adelmo.rendering-service.url")
      .fold("")(_ + "/render")

    val payload2 = req.body.asFormUrlEncoded.getOrElse(Map.empty)
    val payload = Json.toJson(payload2.mapValues(_.headOption.getOrElse("")))
    println(payload)

    WS.url(tempUrl).post(payload).flatMap { response =>
      println(response.body)
      val input = RenderingInput(JobId("123"), RenderingConfig(Pdf, LibreOffice),
        Document(Html, ByteVector.view(response.bodyAsBytes)))
      val json = input.asJson.noSpaces
      WS.url(rendUrl).post(json).map(toResult)
    }
  }

  def toResult(resp: WSResponse): Result =
    Ok(resp.bodyAsBytes).as(contentTypeOf(resp))

  def contentTypeOf(resp: WSResponse): String =
    resp.header("Content-Type").getOrElse("text/plain")
}
