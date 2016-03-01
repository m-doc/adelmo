package org.mdoc.adelmo.client

import japgolly.scalajs.react.{ ReactComponentB, ReactDOM }
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.html.Element
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

  def templateBox =
    ReactComponentB[Unit]("TemplateBox")
      .render(cs => <.div(<.h3("Templates"), templateList(List("template1", "template2"))))
      .build

  def templateList =
    ReactComponentB[List[String]]("TemplateList")
      .render(cs => <.ul(cs.props.map(name => <.li(name))))
      .build

  def templateEntry = ???

  override def main(): Unit = {
    ReactDOM.render(layout(), dom.document.getElementById("root"))
    ReactDOM.render(templateBox(()), dom.document.getElementById("templates"))
    ()
  }
}
