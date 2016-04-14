package org.siny.controller

import com.github.chengpohi.dao.BookMarkDAO._
import com.github.chengpohi.model.BookMark
import com.github.chengpohi.util.ElasticUtil
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
    val result = createBookMark(httpSession.user, bookMark)
    val bookMarkId = ElasticUtil.parseId(result)
    val b = bookMark.copy(id = Some(bookMarkId))
    HttpResponse(ElasticUtil.bookmarkToJson(b), OK)
  }

  def deleteBookMark(httpSession: HttpSession): HttpResponse = {
    val uri = httpSession.httpRequest.getUri
    val bookMarkID = uri.split("/")(2)
    deleteBookMarkById(bookMarkID, httpSession.user)
    HttpResponse("Delete BookMark Ok, Id: " + bookMarkID, OK)
  }
}
