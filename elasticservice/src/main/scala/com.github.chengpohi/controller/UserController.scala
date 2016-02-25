package com.secer.elastic.controller

import com.secer.elastic.model.User
import com.secer.elastic.util.HashUtil.hashString
import com.sksamuel.elastic4s.ElasticDsl._

/**
 * BookMark model
 * Created by com.github.chengpohi on 7/29/15.
 */
object UserController extends ElasticBase {
  val EMAIL_TYPE: String = "email"
  val PASSWORD_TYPE: String = "password"
  val ALL_TYPE: String = "_all"

  def createUserInfo(user: User): String = {
    createUserMapping(user.name)
    indexMapById(user.name, INFO_TYPE, "1", user).getId
  }

  def validateUserLogin(user: User): User = {
    val resp = client.execute {
      search in ALL_TYPE / INFO_TYPE query {
        bool {
          must {
            termQuery(EMAIL_TYPE, user.email.get)
            termQuery(PASSWORD_TYPE, hashString(user.password.get))
          }
        }
      }
    }.await

    resp.getHits.hits().length match {
      case 1 =>
        val hit = resp.getHits.hits()(0).getSource
        val username = hit.get("name").toString
        val email: Some[String] = Some(hit.get(EMAIL_TYPE).toString)
        User(username, email, Some(""), Some(hashString(email.get)))
      case _ => null
    }
  }
}