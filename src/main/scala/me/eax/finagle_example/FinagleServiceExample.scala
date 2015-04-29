package me.eax.finagle_example

import java.nio.charset.Charset

import com.twitter.finagle.Service
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil

import scala.collection.concurrent.TrieMap

class FinagleServiceExample extends Service[HttpRequest, HttpResponse] {
  val kv = TrieMap.empty[String, String]

  def apply(req: HttpRequest): Future[HttpResponse] = {
    Future {
      val resp = Response(req.getProtocolVersion, HttpResponseStatus.OK)
      val key = req.getUri

      req.getMethod match {
        case HttpMethod.GET =>
          kv.get(key) match {
            case None =>
              resp.setStatus(HttpResponseStatus.NOT_FOUND)
            case Some(value) =>
              resp.setContentString(value)
          }
        case HttpMethod.POST =>
          val value = req.getContent.toString(CharsetUtil.UTF_8)
          kv.update(key, value)
        case HttpMethod.DELETE =>
          kv.remove(key)
        case _ =>
          resp.setStatus(HttpResponseStatus.BAD_REQUEST)
      }
      resp
    }
  }
}
