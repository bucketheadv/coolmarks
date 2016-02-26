package org.siny.controller

import com.github.chengpohi.controller.ElasticController.createTab
import com.github.chengpohi.model.Tab
import org.elasticsearch.common.netty.handler.codec.http.HttpResponseStatus.OK
import org.siny.web.response.HttpResponse
import org.siny.web.rest.controller.RestAction
import org.siny.web.session.HttpSession

/**
 * siny
 * Created by com.github.chengpohi on 8/16/15.
 */
object TabController extends RestAction{
  def postTab(httpSession: HttpSession, tab: Tab): HttpResponse = {
    val resultId = createTab(httpSession.user, tab)
    HttpResponse(resultId, OK)
  }
}
