package me.eax.finagle_example

import com.twitter.finagle._
import com.twitter.finagle.http.{Http => _}
import com.twitter.util._

object FinagleExample extends App {
  val service = new FinagleServiceExample
  val server = Http.serve(":8080", service)
  Await.ready(server)
}
