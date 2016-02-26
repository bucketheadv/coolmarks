package com.github.chengpohi.controller

import com.github.chengpohi.ElasticClientConnector
import com.github.chengpohi.model.{Tab, Field}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.SearchType.Scan
import com.sksamuel.elastic4s.mappings.FieldType.{DateType, StringType}
import com.sksamuel.elastic4s.source.{DocumentMap, JsonDocumentSource}
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.sort.SortOrder.ASC
import org.elasticsearch.transport.RemoteTransportException

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * ElasticBase function
 * Created by com.github.chengpohi on 6/28/15.
 */
class ElasticBase {
  lazy val client = ElasticClientConnector.client

  val BOOKMARK_TYPE: String = "bookmark"
  val TAB_TYPE: String = "tab"
  val INFO_TYPE: String = "info"

  def deleteIndex(indexName: String): Unit = {
    client.execute {
      delete index indexName
    }.await
  }

  def deleteById(documentId: String, indexName: String, indexType: String): Boolean = client.execute {
    delete id documentId from s"${indexName}/${indexType}"
  }.await.isFound

  def createUserMapping(indexName: String): Unit = {
    client.execute {
      create index indexName mappings(
        "bookmark" as(
          "name" typed StringType,
          "url" typed StringType,
          "_tab_id" typed StringType index "not_analyzed",
          "created_at" typed DateType
          ),
        "tab" as(
          "name" typed StringType,
          "_tab_id" typed StringType index "not_analyzed",
          "created_at" typed DateType
          ),
        "info" as(
          "name" typed StringType index "not_analyzed",
          "email" typed StringType index "not_analyzed",
          "password" typed StringType index "not_analyzed",
          "created_at" typed DateType
          )
        )
    }.await
  }

  def createIndex(indexName: String) = client.execute {
    create index indexName
  }

  def indexMap(indexName: String, indexType: String, docuemntMap: DocumentMap): String = {
    val resp = client.execute {
      index into indexName / indexType doc docuemntMap
    }.await
    resp.getId
  }

  def indexField(indexName: String, indexType: String, uf: (String, String)): Future[IndexResponse] = client.execute {
    index into indexName / indexType fields uf
  }

  def indexJSON(indexName: String, indexType: String, json: String): String = {
    val resp = client.execute {
      index into indexName / indexType doc new JsonDocumentSource(json)
    }.await
    resp.getId
  }

  def indexMapById(indexName: String, indexType: String, specifyId: String, documentMap: DocumentMap): IndexResponse = client.execute {
    index into indexName / indexType doc documentMap id specifyId
  }.await

  def addField(indexName: String, indexType: String, field: Field): Unit = {
    try {
      val name = field.name
      val value = field.value
      getAllDataByIndexTypeWithIndexName(indexName, indexType).getHits.getHits.map(hit => {
        client execute {
          update id hit.id in indexName + "/" + indexType script s"ctx._source.$name = '$value'"
        }
      })
    } catch {
      case ime: RemoteTransportException => {
        ime.printStackTrace()
      }
      case e: Exception => throw e
    }
  }

  def removeField(indexName: String, indexType: String, field: Field): Unit = {
    try {
      val name = field.name
      getAllDataByIndexTypeWithIndexName(indexName, indexType).getHits.getHits.map(hit => {
        client execute {
          update id hit.id in indexName + "/" + indexType script s"ctx._source.remove('$name')"
        }
      })
    } catch {
      case ime: RemoteTransportException => {
        ime.printStackTrace()
      }
      case e: Exception => throw e
    }
  }

  def getAllDataByIndexTypeWithIndexName(indexName: String, indexType: String): SearchResponse = client.execute {
    search in indexName / indexType query filteredQuery postFilter matchAllFilter start 0 limit Integer.MAX_VALUE sort (
      by field "created_at" ignoreUnmapped true order ASC
      )
  }.await

  def getAllDataByIndexType(indexType: String): SearchResponse = client.execute {
    search in "*" / indexType query filteredQuery postFilter matchAllFilter start 0 limit Integer.MAX_VALUE
  }.await

  def getAllDataByIndexName(indexName: String): SearchResponse = client.execute {
    search in indexName query filteredQuery postFilter matchAllFilter start 0 limit Integer.MAX_VALUE
  }.await

  def getAllDataByScan(indexName: String, indexType: Option[String] = Some("*")): Stream[SearchResponse] = {
    val res = client.execute {
      search in indexName searchType Scan scroll "10m" size 500
    }

    def fetch(previous: SearchResponse) = {
      client.execute {
        search scroll previous.getScrollId
      }
    }

    def toStream(current: Future[SearchResponse]): Stream[SearchResponse] = {
      val result = Await.result(current, Duration.Inf)
      result.getScrollId match {
        case null => result #:: Stream.empty
        case _ => result #:: toStream(fetch(result))
      }
    }
    toStream(res)
  }

  case class MapSource(source: Map[String, AnyRef]) extends DocumentMap {
    override def map = source
  }

  def bulkCopyIndex(indexName: String, response: Stream[SearchResponse], indexType: String, fields: Array[String]) = {
    import scala.collection.JavaConverters._
    response.foreach(r => {
      client.execute {
        bulk(
          r.getHits.getHits.filter(s => s.getType == indexType || indexType == "*").map {
            s => index into indexName / s.getType doc MapSource(s.getSource.asScala.filter(i => fields.contains(i._1)).toMap)
          }: _*
        )
      }
    })
  }

  def bulkUpdateField(indexName: String, response: Stream[SearchResponse], indexType: String, field: (String, String)) = {
    response.foreach(r => {
      client.execute {
        bulk(
          r.getHits.getHits.filter(s => s.getType == indexType || indexType == "*").map {
            s => update(s.getId).in(s"$indexName/$indexType").doc(field)
          }: _*
        )
      }
    })
  }


  def getDocById(indexName: String, indexType: String, docId: String): GetResponse = client.execute {
    get id docId from s"$indexName/$indexType"
  }.await
}
