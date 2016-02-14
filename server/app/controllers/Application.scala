package controllers

import io.circe.generic.auto._
import io.circe.syntax._
import org.mdoc.common.model._
import org.mdoc.common.model.circe._
import org.mdoc.common.model.Format.{ Html, Pdf }
import org.mdoc.common.model.RenderingEngine._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.libs.ws.ning.NingWSClient
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import scodec.bits.ByteVector

case class DocText(body: String)

object Application extends Controller {

  val docTextForm = Form(mapping("body" -> text)(DocText.apply)(DocText.unapply))

  def index = Action {
    Ok(views.html.main(docTextForm))
  }

  val userPost = Action.async(parse.form(docTextForm)) { implicit request =>
    val userData = request.body
    val tmpl = CompleteTemplate(RenderingConfig(Pdf, Wkhtmltopdf), Document(Html, ByteVector.apply(userData.body.getBytes)))

    val wsClient = NingWSClient()
    wsClient.url("http://localhost:8081/render")
      .post(tmpl.asJson.noSpaces)
      .map(res => Ok(res.bodyAsBytes).as(tmpl.cfg.outputFormat.toMediaType.renderString))
  }

  def version = Action {
    Ok(org.mdoc.adelmo.BuildInfo.version)
  }
}
