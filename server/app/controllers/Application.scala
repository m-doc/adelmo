package controllers

import org.mdoc.common.model.Format.{ Pdf, Html }
import org.mdoc.common.model.RenderingEngine.LibreOffice
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.ning.NingWSClient
import play.api.mvc._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import org.mdoc.common.model._
import scodec.bits.ByteVector
import play.api.libs.json._
import play.api.libs.functional.syntax._
import javax.inject.Inject
import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.ws._
import scala.concurrent.duration._

case class DocText(body: String)

object Application extends Controller {

  val docTextForm = Form(mapping("body" -> text)(DocText.apply)(DocText.unapply))

  def index = Action {
    Ok(views.html.main(docTextForm))
  }

  val userPost = Action.async(parse.form(docTextForm)) { implicit request =>
    val userData = request.body
    val tmpl = CompleteTemplate(RenderingConfig(Pdf, LibreOffice), Document(Html, ByteVector.apply(userData.body.getBytes)))

    //Json.toJson(tmpl)
    //Future(Ok(userData.body))
    val wsClient = NingWSClient()
    wsClient.url("http://localhost:8081/render")
      .post("{\"cfg\":{\"outputFormat\":{\"Odt\":{}},\"engine\":{\"LibreOffice\":{}}},\"doc\":{\"format\":{\"Html\":{}},\"body\":\"SGVsbG8=\"}}")
      .map(res => Ok(res.body))
  }
}
