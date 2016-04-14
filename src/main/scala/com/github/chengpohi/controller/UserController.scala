package com.github.chengpohi.controller

import com.github.chengpohi.dao.UserDAO
import com.github.chengpohi.model.User
import com.github.chengpohi.web.cache.LoginUserCache
import com.github.chengpohi.web.response.HttpResponse
import com.github.chengpohi.web.rest.controller.RestAction
import com.github.chengpohi.web.session.HttpSession
import org.jboss.netty.handler.codec.http.HttpResponseStatus._

/**
 * User Controller
 * Created by com.github.chengpohi on 8/1/15.
 */

object UserController extends RestAction {
  val LOGIN_INPUT_TIP: String = "Please input email with password!"
  val LOGIN_ERROR_TIP: String = "Wrong Email or Password."
  val LOGIN_SUCCESSFUL_TIP: String = "Login Successfully"

  def registerUser(user: User): HttpResponse = {
    val resultId = UserDAO.createUserInfo(user)
    HttpResponse(resultId, OK)
  }

  def userLogin(user: User): HttpResponse = {
    UserDAO.validateUserLogin(user) match {
      case u: User =>
        LoginUserCache.LOGINED_USER += u.cookie.get -> u
        HttpResponse(LOGIN_SUCCESSFUL_TIP, OK, Option(u.cookie.get))
      case _ =>
        HttpResponse(LOGIN_ERROR_TIP, BAD_REQUEST)
    }
  }

  def userInfo(httpSession: HttpSession): HttpResponse = {
    HttpResponse("Hello Chengpohi", ACCEPTED)
  }
}
