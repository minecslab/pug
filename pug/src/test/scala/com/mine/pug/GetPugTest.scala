package com.mine.pug

import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach}
import redis.clients.jedis.exceptions.JedisConnectionException

import scala.concurrent.ExecutionContext

/**
  * Created by mine123 on 04/10/2016.
  */
class GetPugTest extends BasePugTest with BeforeAndAfterEach {
  implicit val ec: ExecutionContext = ExecutionContext.global

  val key1 = "key1"
  val value1 = "value1"
  val key2 = "key2"


  override def beforeEach() {
    super.beforeEach()
    val pug = _pugGenerator.getClient()
    val set1 = pug.set(key1, value1)
    pug.close()
  }

  "Pug.aget" should "return same value to what you persist using aset" in {
    val pug = _pugGenerator.getClient()
    val getResult = pug.aget(key1)
    whenReady(getResult) { result =>
      assert(result === value1)
    }
    pug.close()
  }

  it should "return nil if key doesn't exists" in {
    val pug = _pugGenerator.getClient()
    val getResult = pug.aget(key2)
    whenReady(getResult) { result =>
      assert(result === null)
    }
    pug.close()
  }
  it should "return error if key is not type string." in {

  }
  it should "throw exception is redisService is down" in {
    val pug = _pugGenerator.getClient()
    _redisServer.stop()
    val setResult = pug.aget(key1)

    whenReady(setResult.failed) { ex =>
      ex shouldBe an [JedisConnectionException]
    }

    pug.close()
  }
}
