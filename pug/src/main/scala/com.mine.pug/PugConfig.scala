package com.mine.pug

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.JedisPoolConfig;


object PugConfig {
  lazy val DEFAULT_PUG_POOL_CONFIG = new PugPoolConfig()
}

class PugPoolConfig() extends JedisPoolConfig()
