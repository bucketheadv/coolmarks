package com.github.chengpohi.dao

import com.github.chengpohi.model.User
import com.github.chengpohi.util.ElasticUtil
import com.github.chengpohi.util.ElasticUtil.{documentMapToJson, md5Hash}
import org.json4s._
import org.json4s.native.JsonMethods._

/**
  * BookMark model
  * Created by com.github.chengpohi on 7/29/15.
  */
object UserDAO extends BaseDAO {
  implicit val formats = DefaultFormats

  val EMAIL_TYPE: String = "email"
  val PASSWORD_TYPE: String = "password"
  val ALL_TYPE: String = "_all"

  def createUserInfo(user: User): String = {
    createUserMapping(user.name)
    indexMapById(user.name, INFO_TYPE, "1", user)
  }

  def createUserMapping(indexName: String): String = {
    elkRunEngine.run(MAPPINGS, indexName)
  }

  def validateUserLogin(user: User): User = {
    val resp = elkRunEngine.run(TERM_QUERY, ALL_TYPE, INFO_TYPE, ElasticUtil.userLoginJson(user))
    userRetrieve(resp)
  }

  def userRetrieve(resp: String): User = {
    val jObj = parse(resp)
    try {
      val name = jObj \\ "_source" \ "name"
      val email = jObj \\ "_source" \ "email"
      User(name.extract[String], Some(email.extract[String]), Some(""), Some(md5Hash(email.extract[String])))
    } catch {
      case e: Exception => null
    }
  }
}
