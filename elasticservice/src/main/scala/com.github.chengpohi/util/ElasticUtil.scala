package com.github.chengpohi.util

import com.github.chengpohi.model.User
import com.sksamuel.elastic4s.source.DocumentMap
import org.json4s._
import org.json4s.native.Serialization.write

import scala.io.Source

/**
  * HashUtil
  * Created by com.github.chengpohi on 7/2/15.
  */
object ElasticUtil {
  implicit val formats = DefaultFormats
  def md5Hash(source: String): String =
    java.security.MessageDigest.getInstance("MD5").digest(source.getBytes).map(0xFF & _).map {
      "%02x".format(_)
    }.foldLeft("") {
      _ + _
    }

  def readResourcesAsString(fileName: String): String = Source.fromURL(getClass.getResource(fileName)).getLines().mkString("")

  def userLoginJson(user: User): String = {
    write(Map(("email", user.email.get), ("password", md5Hash(user.password.get))))
  }

  implicit def documentMapToJson(documentMap: DocumentMap): String = {
    write(documentMap.map)
  }
}
