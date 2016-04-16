package com.github.chengpohi.controller

import com.github.chengpohi.web.rest.controller.RestAction
import com.github.chengpohi.dao.UserDAO
import com.github.chengpohi.model.Query
import com.github.chengpohi.web.response.HttpResponse
import com.github.chengpohi.web.session.HttpSession
import org.jboss.netty.handler.codec.http.HttpResponseStatus._

/**
  * coolmarks
  * Created by chengpohi on 4/16/16.
  */
object StatisticController extends RestAction {
  def statistic(httpSession: HttpSession, q: Query): HttpResponse = {
    val result: String = UserDAO.elkRunEngine.run(q.query)
    HttpResponse(result, OK)
  }
}
