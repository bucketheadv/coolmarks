package org.siny.controller

import com.github.chengpohi.controller.BookMarkController._
import com.github.chengpohi.model.BookMark
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.siny.web.response.HttpResponse
import org.siny.web.rest.controller.RestAction
import org.siny.web.session.HttpSession

/**
 * siny
 * Created by com.github.chengpohi on 8/16/15.
 */
object BookMarkController extends RestAction{
  def getBookMarks(httpSession: HttpSession): HttpResponse = {
    HttpResponse(getAllBookmarks(httpSession.user), OK)
  }

  def postBookMark(httpSession: HttpSession, bookMark: BookMark): HttpResponse = {
    createBookMark(httpSession.user, bookMark)
    HttpResponse("Create Success", OK)
  }

  def deleteBookMark(httpSession: HttpSession): HttpResponse = {
    val uri = httpSession.httpRequest.getUri
    val bookMarkID = uri.split("/")(2)
    deleteBookMarkById(bookMarkID, httpSession.user)
    HttpResponse("Delete BookMark Ok, Id: " + bookMarkID, OK)
  }
}
