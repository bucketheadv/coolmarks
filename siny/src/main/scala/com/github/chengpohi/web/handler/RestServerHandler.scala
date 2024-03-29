package com.github.chengpohi.web.handler

import com.github.chengpohi.model.User
import com.github.chengpohi.web.cache.LoginUserCache
import com.github.chengpohi.web.response.HttpResponse
import com.github.chengpohi.web.rest.controller.RestController
import org.jboss.netty.channel.{ChannelHandlerContext, ChannelStateEvent, ExceptionEvent, MessageEvent, SimpleChannelUpstreamHandler}
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST
import org.jboss.netty.util.CharsetUtil
import org.json4s._
import org.json4s.native.JsonMethods._
import com.github.chengpohi.web.response.ResponseWriter._
import RestController._
import com.github.chengpohi.web.session.HttpSession
import org.slf4j.LoggerFactory

/**
 * Rest Server Handler to deal Http Request
 * Created by chengpohi on 4/18/15.
 */
class RestServerHandler extends SimpleChannelUpstreamHandler {
  lazy val LOG = LoggerFactory.getLogger(getClass.getName)
  implicit val formats = DefaultFormats

  def uriMatchRule(rule: String, uri: String): Boolean = {
    val ruleRegex = rule.replaceAll("\\{\\w+\\}", ".*").r
    ruleRegex.pattern.matcher(uri).matches()
  }

  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) = {
    val httpRequest = e.getMessage.asInstanceOf[HttpRequest]

    val user = visitHandler(ctx, httpRequest)

    val uri: String = httpRequest.getUri

    LOG.info("IP: " + e.getRemoteAddress + " VISIT URL: " + uri + " Method:" + httpRequest.getMethod)
    val f: (Any, Manifest[_]) = httpRequest.getMethod.getName match {
      case GET => getActions.getOrElse(uri, null)
      case POST => postActions.getOrElse(uri, null)
      case PUT => putActions.getOrElse(uri, null)
      case DELETE => deleteActions.filter(i => uriMatchRule(i._1, uri)).values.head
    }


    val httpSession = HttpSession(user, httpRequest)

    f match {
      case null =>
        writeFile(ctx.getChannel, uri, httpSession)
      case _ =>
        val httpResponse: HttpResponse = executehandler(f, httpSession)
        writeBuffer(ctx.getChannel, httpResponse)
    }
  }

  def executehandler(f: (Any, Manifest[_]), httpSession: HttpSession): HttpResponse = {
    httpSession.httpRequest.getMethod.getName match {
      case GET =>
        def getAction[A] = f._1.asInstanceOf[A => HttpResponse]

        getAction(httpSession)
      case POST =>
        httpSession.user match {
          case null => {
            implicit val mf = f._2
            val rawData = httpSession.httpRequest.getContent.toString(CharsetUtil.UTF_8)
            def postAction[A] = f._1.asInstanceOf[A => HttpResponse]
            postAction(parse(rawData).extract)
          }
          case _ => {
            implicit val mf = f._2
            val rawData = httpSession.httpRequest.getContent.toString(CharsetUtil.UTF_8)
            def postAction[A] = f._1.asInstanceOf[(HttpSession, A) => HttpResponse]
            postAction(httpSession, parse(rawData).extract)
          }
        }

      case PUT =>
        def putAction[A] = f._1.asInstanceOf[A => HttpResponse]

        putAction(httpSession)
      case DELETE =>
        def deleteAction[A] = f._1.asInstanceOf[A => HttpResponse]
        deleteAction(httpSession)
    }
  }

  def visitHandler(ctx: ChannelHandlerContext, httpRequest: HttpRequest): User = {
    if (httpRequest.headers().get(COOKIE) == null) return null
    val user = for {
      c <- httpRequest.headers().get(COOKIE).split(";")
      if c.trim.matches("cookieID=.+")
    } yield LoginUserCache.LOGINED_USER.getOrElse(c.split("=")(1), null)

    user match {
      case u if u.length == 1 && u(0) != null =>
        user(0)
      case _ =>
        null
    }
  }

  @throws(classOf[Exception])
  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent): Unit = {
    LOG.warn("Exception: " + e.toString)
    writeBuffer(ctx.getChannel, "Internal Exception has occurred".getBytes, BAD_REQUEST)
  }


  override def channelDisconnected(ctx: ChannelHandlerContext, e: ChannelStateEvent) = {
    super.channelDisconnected(ctx, e)
  }
}
