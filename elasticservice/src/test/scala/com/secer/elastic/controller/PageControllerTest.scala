package com.secer.elastic.controller

import com.github.chengpohi.controller.PageController
import com.secer.elastic.builder.PageBuilder
import org.scalatest.{BeforeAndAfter, FlatSpec}

/**
 * Page Controller Test
 * Created by com.github.chengpohi on 8/10/15.
 */
class PageControllerTest extends FlatSpec with BeforeAndAfter {
  val page = PageBuilder.build()
  "PageController " should " index page" in {
    val id = PageController.indexPage(page)

    assert(id != null)
  }

}
