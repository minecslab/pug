package com.mine.pug

import com.mine.jediscala.PugGenerator
import org.scalatest.concurrent.{AsyncAssertions, Futures, ScalaFutures}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import redis.clients.jedis.exceptions.JedisConnectionException
import redis.embedded.RedisServer

import scala.concurrent.ExecutionContext
import scala.util.Random

/**
  * Created by mine123 on 03/10/2016.
  */
class SetPugTest extends BasePugTest {

  implicit val ec: ExecutionContext = ExecutionContext.global


  val key1 = "key1"
  val value1 = "value1"
  val key2 = "key2"
  val value2 = "value2"
  val key3 = "key3"
  val value3 = "value3"
  val value4 = "value4"
  val ok = "OK"


  "Pug.aset" should "save data to redis" in {
    val pug = _pugGenerator.getClient()

    val setResult = pug.aset(key1, value1)

    whenReady(setResult) { result =>
      assert(result === ok)

      val getResult = pug.aget(key1)

      pug.close()
      whenReady(getResult) { result =>
        assert(result ===  value1)
      }
    }
  }

  it should "override existing data if key is the same" in {
    val pug = _pugGenerator.getClient()

    val setResult = pug.aset(key2, value2)

    whenReady(setResult) { result =>
      assert(result === ok)
      val getResult = pug.aget(key2)

      whenReady(getResult) { result =>
        assert(result ===  value2)
        val setResult = pug.aset(key2, value3)

        whenReady(setResult) { result =>
          assert(result === ok)
          val getResult = pug.aget(key2)
          pug.close()
          whenReady(getResult) { result =>
            assert(result === value3)
          }
        }
      }
      }
    }
  it should "throws exception once redis server is dead" in {
    val pug = _pugGenerator.getClient()
    _redisServer.stop()
    val setResult = pug.aset(key3, value3)

    whenReady(setResult.failed) { ex =>
      pug.close()
      ex shouldBe an [JedisConnectionException]
    }
  }

}
