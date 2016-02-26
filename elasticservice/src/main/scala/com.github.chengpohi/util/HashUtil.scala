package com.github.chengpohi.util

/**
 * HashUtil
 * Created by com.github.chengpohi on 7/2/15.
 */
object HashUtil {
  def md5Hash(source: String): String =
    java.security.MessageDigest.getInstance("MD5").digest(source.getBytes).map(0xFF & _).map {
      "%02x".format(_)
    }.foldLeft("") {
      _ + _
    }
}
