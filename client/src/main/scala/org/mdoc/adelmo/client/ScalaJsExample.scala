package org.mdoc.adelmo.client

import org.scalajs.dom
import scala.scalajs.js

object ScalaJsExample extends js.JSApp {

  override def main(): Unit = {
    dom.document.getElementById("header").textContent = "Scala.js"
  }
}
