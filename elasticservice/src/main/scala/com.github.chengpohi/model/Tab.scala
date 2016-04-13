package com.github.chengpohi.model

import com.github.chengpohi.util.ElasticUtil
import com.sksamuel.elastic4s.source.DocumentMap

/**
  * BookMark model
  * Created by xiachen on 5/2/15.
  */
case class Tab(id: Option[String] = None, name: String, tabId: Option[String] = None) extends DocumentMap {
  override def map: Map[String, Any] = Map("name" -> name, "_tab_id" -> ElasticUtil.md5Hash(name), "created_at" -> System.currentTimeMillis().toString)
}
