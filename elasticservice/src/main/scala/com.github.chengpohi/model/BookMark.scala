package com.github.chengpohi.model

import com.sksamuel.elastic4s.source.DocumentMap

/**
 * BookMark model
 * Created by xiachen on 5/1/15.
 */
case class BookMark(id: Option[String] = None, name: String, url: String, tabId: Option[String] = Option("1")) extends DocumentMap {
  override def map: Map[String, Any] = Map("name" -> name, "url" -> url, "created_at" -> System.currentTimeMillis().toString,
    "_tab_id" -> tabId.get)
}
