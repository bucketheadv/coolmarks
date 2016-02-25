package com.secer.elastic.util

/**
 * HashUtil
 * Created by com.github.chengpohi on 7/2/15.
 */
object HashUtil {
  def hashString(source: String): String =
    java.security.MessageDigest.getInstance("MD5").digest(source.getBytes()).map(0xFF & _).map {
      "%02x".format(_)
    }.foldLeft("") {
      _ + _
    }
}
