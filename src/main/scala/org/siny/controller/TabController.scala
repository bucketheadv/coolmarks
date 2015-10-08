package org.siny.controller

import com.secer.elastic.model.Tab
import org.elasticsearch.common.netty.util.CharsetUtil
import org.elasticsearch.common.netty.handler.codec.http.HttpResponseStatus.OK
import com.secer.elastic.controller.ElasticController._
import org.siny.web.response.HttpResponse
import org.siny.web.rest.controller.RestAction
import org.json4s.jackson.JsonMethods._
import org.siny.web.session.HttpSession

/**
 * siny
 * Created by chengpohi on 8/16/15.
 */
object TabController extends RestAction{
  def postTab(httpSession: HttpSession, tab: Tab): HttpResponse = {
    val resultId = createTab(httpSession.user, tab)
    HttpResponse(resultId, OK)
  }
}
