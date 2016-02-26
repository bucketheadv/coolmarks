package com.secer.elastic.builder

import java.net.URL

import com.github.chengpohi.model.{Page, IndexField, FetchItem, FieldSelector}
import com.github.chengpohi.util.HashUtil


/**
 * BookMark model
 * Created by com.github.chengpohi on 8/10/15.
 */
object PageBuilder {
  val selector1 = FieldSelector("title", "title")
  val selector2 = FieldSelector("body", "body")
  val fetchItems = FetchItem(new URL("http://www.google.com"), "www", "google", List(selector1, selector2))

  val indexField1 = IndexField("title", "Hello World")
  val indexField2 = IndexField("body", "What's body?")
  val indexFields = List(indexField1, indexField2)


  def build(): Page = Page(null, fetchItems, "1ad33512e77b98807ecde5e5dbd3cb7c", HashUtil.md5Hash(fetchItems.url.toString), indexFields)
}
