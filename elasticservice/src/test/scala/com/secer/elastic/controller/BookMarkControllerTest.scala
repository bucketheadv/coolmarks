package com.secer.elastic.controller

import com.github.chengpohi.controller.BookMarkController
import com.github.chengpohi.model._
import org.scalatest.FlatSpec

/**
 * ElasticController Test
 * Created by xiachen on 5/16/15.
 */
class BookMarkControllerTest extends FlatSpec {
  val user = User("pipi" + System.currentTimeMillis(), Option("test123"))
  val tab = Tab(Option(""), "tino")
  val bookMark = BookMark(Option(""), "jack", "http://www.baidu.com", Some("somemd5asdfasdf"))

  "ElasticController " should " create user tab" in {
    val resultId = BookMarkController.createTab(user, tab)
    assert(resultId != null)
  }


  "ElasticController" should "delete bookMark by id" in {
    /*val id = ElasticController.getBookMarksWithObject(user)(0).getId
    ElasticController.deleteBookMarkById(id, user)*/
  }
}
