package org.siny.app

import org.siny.controller.BookMarkController.{postBookMark, _}
import org.siny.controller.TabController._
import org.siny.controller.UserAction.{registerUser, userInfo, userLogin}
import org.siny.web.app.Siny
import org.siny.web.rest.controller.RestController._

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

    registerHandler(GET, "/bookmark", getBookMarks)
    registerHandler(GET, "/user.html", userInfo)
    registerHandler(DELETE, "/bookmark/{bookmarkID}", deleteBookMark)
    registerHandler(DELETE, "/tab/{tabID}", deleteTab)
  }
}
