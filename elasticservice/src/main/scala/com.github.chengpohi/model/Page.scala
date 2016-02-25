package com.secer.elastic.model


import com.sksamuel.elastic4s.source.DocumentMap
import org.jsoup.nodes.Document

/**
 * Created by xiachen on 12/16/14.
 */
case class Page(doc: Document, fetchItem: FetchItem, md5: String, urlMd5: String, indexes: List[IndexField]) extends DocumentMap {
  override def map: Map[String, Any] = {
    indexes.groupBy(k => k.field).map(k => (k._2(0).field, k._2(0).content)) ++ Map(
      "md5" -> md5,
      "url" -> fetchItem.url.toString,
      "created_at" -> System.currentTimeMillis()
    )
  }
}
