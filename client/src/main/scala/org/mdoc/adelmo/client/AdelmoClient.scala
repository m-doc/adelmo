package org.mdoc.adelmo.client

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react._
import org.scalajs.dom
import org.scalajs.dom.html.Element
import org.scalajs.jquery._
import scala.scalajs.js
import scala.scalajs.js.JSApp

object AdelmoClient extends JSApp {

  def navbar: ReactTagOf[Element] =
    <.nav(
      ^.className := "navbar navbar-default navbar-fixed-top",
      <.div(
        ^.className := "container",
        <.div(
          ^.className := "navbar-header",
          <.a(^.className := "navbar-brand", ^.href := "#", "m-doc")
        )
      )
    )

  def layout: ReactTagOf[Element] =
    <.div(
      navbar,
      <.div(
        ^.className := "container",
        <.div(
          ^.className := "jumbotron",
          <.div(^.id := "templates")
        )
      )
    )

  def templateList =
    ReactComponentB[List[String]]("TemplateList")
      .render(cs => <.ul(cs.props.map(name => templateEntry(name))))
      .build

  def templateEntry =
    ReactComponentB[String]("TemplateEntry")
      .render(cs => <.li(cs.props))
      .build

  case class Template(name: String, placeholders: List[String])

  case class State(availableTemplates: List[String], selectedTemplate: Option[Template])

  class Backend($: BackendScope[Unit, State]) {
    def templateSelected(n: String)(e: ReactEventH) = {
      // js.dom.alert(n)
      $.modState(_.copy(selectedTemplate = Some(Template("blob", Nil))))
    }

    def render(state: State): ReactTagOf[Element] =
      <.div(
        <.h3("Templates"),
        <.ul(state.availableTemplates.map(name => <.li(^.onClick ==> templateSelected(name), name)))
      )

  }

  val TemplateApp = ReactComponentB[Unit]("TemplateApp")
    .initialState(State(List("template1", "template2"), None))
    .renderBackend[Backend]
    .componentDidMount(scope => Callback {
      jQuery.ajax(js.Dynamic.literal(
        url = "/api/templates",
        success = { (data: js.Any, textStatus: String, jqXHR: JQueryXHR) =>

        val d = data.asInstanceOf[js.Array[js.Dynamic]].to[List].map(_.name.toString)
        scope.modState(_.copy(availableTemplates = d)).runNow()
        //scope.setState(scope.state.copy(availableTemplates = d))

        dom.console.log(s"data=${d},text=$textStatus,jqXHR=$jqXHR")
      },
        error = { (jqXHR: JQueryXHR, textStatus: String, errorThrow: String) =>
        dom.console.log(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
      },
        `type` = "GET"
      ).asInstanceOf[JQueryAjaxSettings])
    })
    .buildU

  override def main(): Unit = {
    ReactDOM.render(layout(), dom.document.getElementById("root"))
    ReactDOM.render(TemplateApp(), dom.document.getElementById("templates"))
    ()
  }

  /*

  def templateBox =
    ReactComponentB[Unit]("TemplateBox")
      .render(cs => <.div(<.h3("Templates"), templateList(List("template1", "template2"))))
      .build

  */
}
