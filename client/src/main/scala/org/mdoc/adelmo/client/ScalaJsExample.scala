package org.mdoc.adelmo.client

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object ScalaJsExample extends js.JSApp {

  override def main(): Unit = {
    println("hallo")
    dom.document.getElementById("header").textContent = "Scala.js"
    dom.window.alert("Hi from Scala-js-dom")
  }
}
