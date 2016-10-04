package com.mine.jediscala
import com.mine.pug.{Pug, PugPool}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{BinaryJedis, JedisPool, JedisPoolConfig, JedisSentinelPool};


/**
  * Implementor of [[RedisClientGeneratorLike]]
  */
class PugGenerator(poolConfig: GenericObjectPoolConfig, host: String,
                   port: Int ) extends RedisClientGeneratorLike {

  import scala.collection.JavaConversions._

  private lazy val pool = new PugPool(poolConfig, host, port)

  override def getBinaryClient() = pool.getResource.asInstanceOf[BinaryJedis]

  override def getPool() = pool

  override def getClient() = pool.getResource()
}

trait RedisClientGeneratorLike {
  /**
    * Get [[BinaryJedis]] client from the pool
    * @return
    */
  def getBinaryClient(): BinaryJedis

  /**
    * Get Pool of [[redis.clients.jedis.Jedis]]
    * @return
    */
  def getPool(): redis.clients.util.Pool[Pug]

  /**
    * Get [[redis.clients.jedis.Jedis]] client from the pool
    * @return
    */
  def getClient(): Pug
}