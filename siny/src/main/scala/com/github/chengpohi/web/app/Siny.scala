package com.github.chengpohi.web.app

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import com.github.chengpohi.web.handler.RestServerHandler
import com.typesafe.config.ConfigFactory
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.channel.{ChannelPipeline, ChannelPipelineFactory, Channels}
import org.jboss.netty.handler.codec.http.{HttpChunkAggregator, HttpRequestDecoder, HttpResponseEncoder}
import org.jboss.netty.handler.stream.ChunkedWriteHandler
import org.slf4j.LoggerFactory

/**
 * Created by xiachen on 4/17/15.
 */
trait Siny {
  def initialize() {
    this.registerPath()

    lazy val LOG = LoggerFactory.getLogger(getClass.getName)
    lazy val config = ConfigFactory.load()
    val serverBootStrap = new ServerBootstrap(new NioServerSocketChannelFactory(
      Executors.newCachedThreadPool(),
      Executors.newCachedThreadPool()
    ))

    serverBootStrap.setPipelineFactory(new ChannelPipelineFactory() {
      override def getPipeline: ChannelPipeline = {
        val pipeline: ChannelPipeline = Channels.pipeline()
        pipeline.addLast("decoder", new HttpRequestDecoder())
        pipeline.addLast("aggregator", new HttpChunkAggregator(65536))
        pipeline.addLast("encoder", new HttpResponseEncoder())
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler())

        pipeline.addLast("handler", new RestServerHandler())
        pipeline
      }
    })

    serverBootStrap.setOption("child.reuseAddress", true)
    serverBootStrap.setOption("child.tcpNoDelay", true)
    serverBootStrap.setOption("child.keepAlive", true)

    // Bind and start to accept incoming connections.
    serverBootStrap.bind(new InetSocketAddress(config.getInt("http.port")))
    LOG.info("Server Started, IP: " + config.getString("http.interface") + ", Port: " + config.getInt("http.port"))
  }

  def registerPath(): Unit = ???
}

