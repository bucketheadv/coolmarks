package com.github.chengpohi.dao

import com.github.chengpohi.ElasticClientConnector
import com.github.chengpohi.api.ElasticCommand
import com.github.chengpohi.helper.ResponseGenerator
import com.github.chengpohi.parser.{ELKCommand, ELKParser, ParserUtils}

/**
  * elasticshell
  * Created by chengpohi on 4/4/16.
  */

object ELKComponentRegistry {
  private[this] val client = ElasticClientConnector.client
  private[this] val elasticCommand = new ElasticCommand(client)
  val responseGenerator = new ResponseGenerator
  private[this] val elkCommand = new ELKCommand(elasticCommand, responseGenerator)
  private[this] val parserUtils = new ParserUtils
  val elkParser = new ELKParser(elkCommand, parserUtils)
}
