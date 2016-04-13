package com.github.chengpohi

import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import com.typesafe.config.ConfigFactory
import org.elasticsearch.common.settings.Settings

/**
  * Created by com.github.chengpohi on 3/19/15.
  */
object ElasticClientConnector {
  lazy val indexConfig = ConfigFactory.load("application.conf").getConfig("elastic")
  lazy val settings = Settings.settingsBuilder()
    .put("http.enabled", "false")
    .put("transport.enable", "false")
    .put("path.home", indexConfig.getString("path.home"))
    .put("cluster.name", indexConfig.getString("cluster.name"))
    .build()

  val host: String = indexConfig.getString("host")
  val port: Int = indexConfig.getInt("port")
  val uri = ElasticsearchClientUri(s"elasticsearch://$host:$port")
  val client = ElasticClient.local(settings)
}
