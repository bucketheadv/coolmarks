package com.secer.elastic.model

import com.secer.elastic.util.HashUtil
import com.sksamuel.elastic4s.source.DocumentMap

/**
 * Created by xiachen on 5/1/15.
 */
case class User(name: String, email: Option[String] = None, password: Option[String] = None, cookie: Option[String] = None) extends DocumentMap {
  override def map: Map[String, Any] = Map(
    "name" -> name,
    "password" -> HashUtil.hashString(password.get),
    "email" -> email.get
  )
}
