package com.github.chengpohi.dao

import com.github.chengpohi.ELKRunEngine
import com.typesafe.config.ConfigFactory

/**
 * ElasticBase function
 * Created by com.github.chengpohi on 6/28/15.
 */
class BaseDAO {
  val elkRunEngine = new ELKRunEngine(ELKComponentRegistry)
  val ELK_PROPERTIES = ConfigFactory.load("elk.properties")

  val BOOKMARK_TYPE: String = "bookmark"
  val TAB_TYPE: String = "tab"
  val INFO_TYPE: String = "info"
  val MAPPINGS: String = ELK_PROPERTIES.getString("user_mapping")
  val INDEX: String = ELK_PROPERTIES.getString("index_doc")
  val INDEX_DOC_ID: String = ELK_PROPERTIES.getString("index_doc_id")
  val TERM_QUERY: String = ELK_PROPERTIES.getString("term_query")
  val DELETE_DOC_ID: String = ELK_PROPERTIES.getString("delete_doc_id")
  val GET_ALL_BOOKMARKS: String = ELK_PROPERTIES.getString("get_all_bookmarks")

  def indexJson(indexName: String, indexType: String, jsonDoc: String): String = {
    elkRunEngine.run(INDEX, indexName, indexType, jsonDoc)
  }

  def indexMapById(indexName: String, indexType: String, specifyId: String, jsonDoc: String): String =
    elkRunEngine.run(INDEX_DOC_ID, indexName, indexType, jsonDoc, specifyId)
}
