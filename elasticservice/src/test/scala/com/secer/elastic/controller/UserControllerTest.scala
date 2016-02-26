package com.secer.elastic.controller

import com.github.chengpohi.controller.{UserController, ElasticController}
import com.github.chengpohi.model.User
import com.github.chengpohi.util.HashUtil
import org.scalatest.FlatSpec

/**
 * BookMark model
 * Created by com.github.chengpohi on 7/29/15.
 */
class UserControllerTest extends FlatSpec {
  val user = User("com.github.chengpohi", Some("com.github.chengpohi@gmail.com"), Some("123QQQ"))
  val user1 = User("com.github.chengpohi", Some("com.github.chengpohi@gmail.com"), Some("123456"))

  "UserController " should " create user info" in {
    val resultId = UserController.createUserInfo(user)
    assert(resultId != null)
  }


  "UserController " should " validate user info with wrong password" in {
    val userWithoutCorrectPassword = User("", Some("com.github.chengpohi@gmail.com"), Some("123456"))
    Thread.sleep(1000)
    assert(UserController.validateUserLogin(userWithoutCorrectPassword) == null)
  }
  ElasticController.deleteIndexByIndexName(user.name)
}
