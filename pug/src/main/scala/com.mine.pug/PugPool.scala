package com.mine.pug

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{JedisPool, Protocol, JedisFactory}
import redis.clients.util.Pool


/**
  * Created by mine123 on 02/10/2016.
  */
class PugPool(poolConfig: GenericObjectPoolConfig, host: String, port: Int)
  extends Pool[Pug](poolConfig, new PugFactory(host, port, Protocol
    .DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, null, Protocol
    .DEFAULT_DATABASE, null, false, null, null, null)) {




  override def getResource(): Pug = {
    val pug = super.getResource()
    pug.ds = this
    pug
  }
}
