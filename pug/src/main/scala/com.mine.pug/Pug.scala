package com.mine.pug

import javax.net.ssl.{HostnameVerifier, SSLParameters, SSLSocketFactory}

import redis.clients.jedis.Jedis
import redis.clients.util.Pool

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by mine123 on 02/10/2016.
  */
class Pug(host: String, port: Int, connectionTimeout: Int, soTimeout: Int,
          ssl: Boolean, sSLSocketFactory: SSLSocketFactory,
          sSLParameters: SSLParameters,
          hostnameVerifier: HostnameVerifier) extends Jedis(host, port,
  connectionTimeout, soTimeout, ssl, sSLSocketFactory, sSLParameters,
  hostnameVerifier) with AsyncPug {

  var ds: Pool[Pug] = _

  override def close() {
    if (ds != null) if (client.isBroken) this.ds.returnBrokenResource(this)
    else this.ds.returnResource(this)
    else client.close()
  }

  override def aset(key: String, value: String)(implicit ec: ExecutionContext):
  Future[String] = Future {
    set(key, value)
  }

  override def aget(key: String)(implicit ec: ExecutionContext):
  Future[String] = Future {
    get(key)
  }
}
