package com.github.chengpohi.model

import java.net.URL

/**
 * Created by xiachen on 12/13/14.
 */
case class FetchItem(url: URL, indexName: String, indexType: String, selectors: List[FieldSelector], urlRegex: Option[String] = None) {
  def filterByRule(): Boolean = {
    //UrlFilter.filterByRule(url)
    false
  }

  def filterOrFetch(fetch: (FetchItem) => Unit) = {
    filterByRule() match {
      case false => fetch(this)
      case _ =>
    }
  }
}
