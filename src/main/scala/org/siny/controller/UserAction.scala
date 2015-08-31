package org.siny.controller

import com.secer.elastic.controller.UserController
import com.secer.elastic.model.User
import org.elasticsearch.common.netty.handler.codec.http.HttpResponseStatus
import org.elasticsearch.common.netty.handler.codec.http.HttpResponseStatus.{BAD_REQUEST, OK}
import org.elasticsearch.common.netty.util.CharsetUtil
import org.siny.web.cache.LoginUserCache
import org.siny.web.response.HttpResponse
import org.siny.web.rest.controller.RestAction
import org.json4s._
import org.json4s.native.JsonMethods._
import org.siny.web.session.HttpSession

/**
 * User Controller
 * Created by chengpohi on 8/1/15.
 */

object UserAction extends RestAction {
  val LOGIN_INPUT_TIP: String = "Please input email with password!"
  val LOGIN_ERROR_TIP: String = "Wrong Email or Password."
  val LOGIN_SUCCESSFUL_TIP: String = "Login Successfully"

  def registerUser(httpSession: HttpSession): HttpResponse = {
    val rawUser = httpSession.httpRequest.getContent.toString(CharsetUtil.UTF_8)

    val user = parse(rawUser).extract[User]

    val resultId = UserController.createUserInfo(user)
    HttpResponse(resultId, OK)
  }

  def userLogin(httpSession: HttpSession): HttpResponse = {
    val rawUser = parse(httpSession.httpRequest.getContent.toString(CharsetUtil.UTF_8))

    val user = User("",
      Option((rawUser \\ "email").extract[String]),
      Option((rawUser \\ "password").extract[String])
    )

    UserController.validateUserLogin(user) match {
      case u: User =>
        LoginUserCache.LOGINED_USER += u.cookie.get -> u
        HttpResponse(LOGIN_SUCCESSFUL_TIP, OK, Option(u.cookie.get))
      case _ =>
        HttpResponse(LOGIN_ERROR_TIP, BAD_REQUEST)
    }
  }

  def userInfo(httpSession: HttpSession): HttpResponse = {
    HttpResponse("Hello Chengpohi", HttpResponseStatus.ACCEPTED)
  }
}
