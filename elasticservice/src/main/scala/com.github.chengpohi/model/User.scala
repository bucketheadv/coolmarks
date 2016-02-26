package com.github.chengpohi.model

import com.github.chengpohi.util.HashUtil
import com.sksamuel.elastic4s.source.DocumentMap

/**
 * Created by xiachen on 5/1/15.
 */
case class User(name: String, email: Option[String] = None, password: Option[String] = None, cookie: Option[String] = None) extends DocumentMap {
  override def map: Map[String, Any] = Map(
    "name" -> name,
    "password" -> HashUtil.md5Hash(password.get),
    "email" -> email.get
  )
}
