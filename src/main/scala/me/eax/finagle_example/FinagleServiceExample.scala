package me.eax.finagle_example

import com.twitter.finagle.Service
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

import scala.collection.concurrent.TrieMap

class FinagleServiceExample extends Service[HttpRequest, HttpResponse] {
  private val kv = TrieMap.empty[String, String]
  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  logger.info(s"service started")

  def apply(req: HttpRequest): Future[HttpResponse] = {
    Future {
      val resp = Response(req.getProtocolVersion, HttpResponseStatus.OK)
      val key = req.getUri

      req.getMethod match {
        case HttpMethod.GET =>
          logger.debug(s"reading $key")
          kv.get(key) match {
            case None =>
              resp.setStatus(HttpResponseStatus.NOT_FOUND)
            case Some(value) =>
              resp.setContentString(value)
          }
        case HttpMethod.POST =>
          logger.debug(s"writing $key")
          val value = req.getContent.toString(CharsetUtil.UTF_8)
          kv.update(key, value)
        case HttpMethod.DELETE =>
          logger.debug(s"deleting $key")
          kv.remove(key)
        case _ =>
          logger.error(s"bad request: $req")
          resp.setStatus(HttpResponseStatus.BAD_REQUEST)
      }
      resp
    }
  }
}
