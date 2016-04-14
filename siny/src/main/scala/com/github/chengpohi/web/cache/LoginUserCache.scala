package com.github.chengpohi.web.cache

import java.util.concurrent.ConcurrentHashMap

import com.github.chengpohi.model.User

import scala.collection._
import scala.collection.convert.decorateAsScala._


/**
 * BookMark model
 * Created by chengpohi on 7/31/15.
 */
object LoginUserCache {
  val LOGINED_USER: concurrent.Map[String, User] = new ConcurrentHashMap[String, User]().asScala
}
