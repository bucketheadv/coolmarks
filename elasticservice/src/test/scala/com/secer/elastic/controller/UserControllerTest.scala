package com.secer.elastic.controller

import com.github.chengpohi.dao.{UserDAO, BookMarkDAO}
import com.github.chengpohi.model.User
import com.github.chengpohi.util.ElasticUtil
import org.scalatest.{BeforeAndAfter, FlatSpec}

/**
 * BookMark model
 * Created by com.github.chengpohi on 7/29/15.
 */
class UserControllerTest extends FlatSpec with BeforeAndAfter{
  val user = User("com.github.chengpohi", Some("com.github.chengpohi@gmail.com"), Some("123QQQ"))
  val user1 = User("com.github.chengpohi", Some("com.github.chengpohi@gmail.com"), Some("123456"))

  before {
    UserDAO.createUserInfo(user)
  }

  "UserController " should " validate user info with wrong password" in {
    val userWithoutCorrectPassword = User("", Some("com.github.chengpohi@gmail.com"), Some("123456"))
    Thread.sleep(1000)
    assert(UserDAO.validateUserLogin(userWithoutCorrectPassword) == null)
  }
}
