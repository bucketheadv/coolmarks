package org.siny.controller

import com.github.chengpohi.dao.BookMarkDAO.{createTab, deleteTabById}
import com.github.chengpohi.model.Tab
import com.github.chengpohi.util.ElasticUtil
import org.jboss.netty.handler.codec.http.HttpResponseStatus.OK
import org.siny.web.response.HttpResponse
import org.siny.web.rest.controller.RestAction
import org.siny.web.session.HttpSession

/**
 * siny
 * Created by com.github.chengpohi on 8/16/15.
 */
object TabController extends RestAction {
  def postTab(httpSession: HttpSession, tab: Tab): HttpResponse = {
    val resultId = createTab(httpSession.user, tab)
    val tabId = ElasticUtil.parseId(resultId)
    val t = tab.copy(id = Some(tabId))
    HttpResponse(ElasticUtil.tabToJson(t), OK)
  }

  def deleteTab(httpSession: HttpSession): HttpResponse = {
    val uri = httpSession.httpRequest.getUri
    val tabID = uri.split("/")(2)
    deleteTabById(tabID, httpSession.user)
    HttpResponse("Delete tab Ok, Id: " + tabID, OK)
  }
}
