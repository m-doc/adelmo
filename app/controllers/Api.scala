package controllers

import io.circe.generic.auto._
import io.circe.syntax._
import org.mdoc.common.model._
import org.mdoc.common.model.circe._
import org.mdoc.common.model.Format.{ Html, Pdf }
import org.mdoc.common.model.RenderingEngine.LibreOffice
import play.api.Play.current
import play.api.libs.json.{ Json, JsValue }
import play.api.libs.ws.{ WS, WSResponse }
import play.api.mvc.{ Action, AnyContent, Controller, Result }
import scala.concurrent.ExecutionContext.Implicits.global
import scodec.bits.ByteVector

object Api extends Controller {

  def templates: Action[AnyContent] = Action.async {
    val url = templateServiceUrl + "/api/template-views"
    WS.url(url).get().map(toResult)
  }

  def placeholders(name: String): Action[AnyContent] = Action.async {
    val url = templateServiceUrl + "/api/template-placeholders/" + name
    WS.url(url).get().map(toResult)
  }

  def renderTemplate(name: String): Action[AnyContent] = Action.async { req =>
    val processUrl = templateServiceUrl + "/api/process/" + name
    val renderUrl = renderingServiceUrl + "/render"

    val formData = req.body.asFormUrlEncoded.getOrElse(Map.empty)
    val formDataAsJson = formUrlEncodedToJsonMap(formData)

    WS.url(processUrl).post(formDataAsJson).flatMap { resp =>
      val input = RenderingInput(
        JobId("123"),
        RenderingConfig(Pdf, LibreOffice), // TODO: output format and renderer should be selectable
        Document(Html, ByteVector.view(resp.bodyAsBytes))
      ) // TODO: extract input format from name
      val inputAsJson = input.asJson.noSpaces
      WS.url(renderUrl).post(inputAsJson).map(toResult)
    }
  }

  val renderingServiceUrl: String =
    current.configuration.getString("adelmo.rendering-service.url").getOrElse("")

  val templateServiceUrl: String =
    current.configuration.getString("adelmo.template-service.url").getOrElse("")

  def formUrlEncodedToJsonMap(map: Map[String, Seq[String]]): JsValue =
    Json.toJson(map.mapValues(_.mkString(" ")))

  def toResult(resp: WSResponse): Result =
    Ok(resp.bodyAsBytes).as(contentTypeOf(resp))

  def contentTypeOf(resp: WSResponse): String =
    resp.header("Content-Type").getOrElse("text/plain; charset=utf-8")
}
