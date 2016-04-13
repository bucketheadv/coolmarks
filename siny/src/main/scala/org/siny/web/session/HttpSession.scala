package org.siny.web.session

import com.github.chengpohi.model.User
import org.jboss.netty.handler.codec.http.HttpRequest

/**
 * HttpSession for communicating
 * Created by chengpohi on 8/1/15.
 */
case class HttpSession (user: User, httpRequest: HttpRequest)
