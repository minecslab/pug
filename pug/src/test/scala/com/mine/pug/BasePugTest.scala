package com.mine.pug

import com.mine.jediscala.PugGenerator
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec, Matchers}
import org.scalatest.concurrent.{AsyncAssertions, ScalaFutures}
import redis.embedded.RedisServer

import scala.util.Random

/**
  * Created by mine123 on 04/10/2016.
  */
abstract class BasePugTest extends FlatSpec with Matchers with BeforeAndAfterEach
  with ScalaFutures with AsyncAssertions {

  val range = 9000 to 10000
  var _redisServer: RedisServer = _

  var _pugGenerator: PugGenerator = _

  override def beforeEach() {
    val port = range(Random.nextInt(range.length))
    _redisServer = new RedisServer(port)
    _redisServer.start()

    _pugGenerator = new PugGenerator(PugConfig.DEFAULT_PUG_POOL_CONFIG,
      "localhost", port)

  }

  override def afterEach() {
    _redisServer.stop()
  }

}
