package com.github.chengpohi.app

import com.github.chengpohi.controller.BookMarkController.{postBookMark, _}
import com.github.chengpohi.controller.StatisticController.statistic
import com.github.chengpohi.controller.TabController._
import com.github.chengpohi.controller.UserController._
import com.github.chengpohi.web.app.Siny
import com.github.chengpohi.web.rest.controller.RestController._

/**
 * siny
 * Created by com.github.chengpohi on 8/16/15.
 */
object CoolMark extends Siny {
  def main(args: Array[String]): Unit = {
    this.initialize()
  }

  override def registerPath(): Unit = {
    registerHandler(POST, "/register.html", registerUser)
    registerHandler(POST, "/login.html", userLogin)
    registerHandlerWithHttpSession(POST, "/bookmark", postBookMark)
    registerHandlerWithHttpSession(POST, "/tab", postTab)
    registerHandlerWithHttpSession(POST, "/statistic", statistic)

    registerHandler(GET, "/bookmark", getBookMarks)
    registerHandler(GET, "/user.html", userInfo)
    registerHandler(DELETE, "/bookmark/{bookmarkID}", deleteBookMark)
    registerHandler(DELETE, "/tab/{tabID}", deleteTab)
  }
}
