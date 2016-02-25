package com.secer.elastic.controller

import com.secer.elastic.model.{BookMark, Tab, User}
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.sort.SortOrder.ASC
import org.elasticsearch.transport.RemoteTransportException
import org.slf4j.LoggerFactory

import scala.language.implicitConversions
import scala.collection.JavaConverters._
import scala.util.parsing.json.JSONObject

/**
 * Elastic Controller
 * Created by com.github.chengpohi on 3/1/15.
 */
object ElasticController extends ElasticBase {
  lazy val LOG = LoggerFactory.getLogger(getClass.getName)


  def createBookMark(user: User, bookMark: BookMark): String = indexMap(user.name, BOOKMARK_TYPE, bookMark)

  def createTab(user: User, tab: Tab): String = indexMap(user.name, TAB_TYPE, tab)

  def getBookMarksWithJson(user: User): String = {
    val result = for (hit <- getTabsWithObject(user)) yield {
      try {
        val resp = client.execute {
          search in user.name / BOOKMARK_TYPE query filteredQuery postFilter
            termFilter("_tab_id", hit.getSource.getOrDefault("_tab_id", "")) start 0 limit Integer.MAX_VALUE
        }.await
        "\"" + hit.getSource.get("name") + "\" : { \"marks\": " + searchHitsToJSONString(resp.getHits.getHits) + ", \"id\": \"" + hit.getId + "\"}"
      } catch {
        case ime: RemoteTransportException => {
          ime.printStackTrace()
          null
        }
        case e: Exception => throw e
      }
    }

    result.mkString("{", ",", "}")
  }

  def getTabsWithObject(user: User): Array[SearchHit] = {
    try {
      getAllDataByIndexTypeWithIndexName(user.name, TAB_TYPE).getHits.getHits
    } catch {
      case ime: RemoteTransportException => {
        ime.printStackTrace()
        null
      }
      case e: Exception => throw e
    }
  }

  def getBookMarksWithUser(user: User): Array[SearchHit] = {
    try {
      getAllDataByIndexTypeWithIndexName(user.name, BOOKMARK_TYPE).getHits.getHits
    } catch {
      case ime: RemoteTransportException => null
      case e: Exception => throw e
    }
  }

  def getAllBookMarks: Array[SearchHit] = {
    try {
      getAllDataByIndexType(BOOKMARK_TYPE).getHits.getHits
    } catch {
      case ime: RemoteTransportException => null
      case e: Exception => throw e
    }
  }

  def deleteBookMarkById(_id: String, user: User): Unit = {
    client execute {
      delete id _id from user.name + "/" + BOOKMARK_TYPE
    }
  }

  def deleteIndexByIndexName(indexName: String): Unit = {
    client.execute {
      delete index indexName
    }.await
  }

  def updateBookMarkById(user: User, bookMark: BookMark): Unit = {
    client execute {
      update id bookMark.id.get in user.name + "/" + BOOKMARK_TYPE doc bookMark.map
    }
  }

  implicit def searchHitsToJSONString(hits: Array[SearchHit]): String = {
    val result = for (hit <- hits) yield {
      hit.getSource.put("id", hit.getId)
      JSONObject(hit.getSource.asScala.toMap).toString()
    }
    result.mkString("[", ",", "]")
  }
}
