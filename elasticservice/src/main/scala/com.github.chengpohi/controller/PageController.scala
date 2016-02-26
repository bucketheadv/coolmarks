package com.github.chengpohi.controller

import com.github.chengpohi.model._
import com.github.chengpohi.util.HashUtil
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.SimpleAnalyzer
import com.sksamuel.elastic4s.mappings.FieldType.{DateType, StringType}
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.transport.RemoteTransportException
import org.slf4j.LoggerFactory


/**
 * Page Controller
 * Created by com.github.chengpohi on 8/10/15.
 */
object PageController extends ElasticBase {
  lazy val LOG = LoggerFactory.getLogger(getClass.getName)

  def createPageMapping(indexName: String, indexType: String): Unit = {
    client.execute {
      create index indexName mappings (
        indexType as(
          "_md5" typed StringType index NotAnalyzed,
          "url" typed StringType analyzer SimpleAnalyzer,
          "created_at" typed DateType
          )
        )
    }.await
  }

  def pageWhetherExist(fetchItem: FetchItem): Boolean = {
    try {
      val response: GetResponse =
        getDocById(fetchItem.indexName, fetchItem.indexType, HashUtil.md5Hash(fetchItem.url.toString))
      response.isExists
    } catch {
      case e: RemoteTransportException => false
    }
  }

  def deletePage(page: Page): Boolean = deleteById(page.urlMd5, page.fetchItem.indexName, page.fetchItem.indexType)

  def indexPage(page: Page): String = {
    indexMapById(page.fetchItem.indexName,
      page.fetchItem.indexType,
      page.urlMd5,
      page
    ).getId
  }
}
