package com.github.chengpohi.dao

import com.github.chengpohi.model._
import com.github.chengpohi.util.ElasticUtil.documentMapToJson
import org.slf4j.LoggerFactory

import scala.language.implicitConversions

/**
 * Elastic Controller
 * Created by com.github.chengpohi on 3/1/15.
 */
object BookMarkDAO extends BaseDAO {
  lazy val LOG = LoggerFactory.getLogger(getClass.getName)


  def createBookMark(user: User, bookMark: BookMark): String =
    indexJson(user.name, BOOKMARK_TYPE, bookMark)

  def createTab(user: User, tab: Tab): String = indexJson(user.name, TAB_TYPE, tab)

  def deleteBookMarkById(_id: String, user: User): String =
    elkRunEngine.run(DELETE_DOC_ID, user.name, BOOKMARK_TYPE, _id)

  def deleteTabById(_id: String, user: User): Unit = {
    elkRunEngine.run(DELETE_DOC_ID, user.name, TAB_TYPE, _id)
  }

  def getAllBookmarks(user: User): String = {
    elkRunEngine.run(GET_ALL_BOOKMARKS, user.name, BOOKMARK_TYPE, user.name, TAB_TYPE, "_tab_id")
  }
}
