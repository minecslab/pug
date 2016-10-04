package com.mine.pug

import javax.net.ssl.{HostnameVerifier, SSLParameters, SSLSocketFactory}

import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.{PooledObject, PooledObjectFactory}
import redis.clients.jedis.exceptions.JedisException

/**
  * Created by mine123 on 03/10/2016.
  */
class PugFactory(host: String, port: Int, connectionTimeout: Int,
                 soTimeout: Int, password: String, database: Int, clientName:
                 String, ssl: Boolean, sslSocketFactory: SSLSocketFactory,
                 sslParameters: SSLParameters, hostnameVerifier: HostnameVerifier) extends
  PooledObjectFactory[Pug] {

  override def destroyObject(p: PooledObject[Pug]): Unit = {
    val pug = p.getObject();
    if (pug.isConnected()) {
      try {
        try {
          pug.quit()
        } catch {
          case e: Exception => {
            //TODO LOG error
          }
        }
        pug.disconnect()
      } catch {
        case e: Exception => {
          //TODO LOG error
        }
      }
    }
  }

  override def validateObject(p: PooledObject[Pug]): Boolean = {
    val pug = p.getObject();

    try {
      return host.equals(pug.getClient().getHost()) && port == pug
        .getClient().getPort() && pug.isConnected() && pug.ping()
        .equals("PONG")
    } catch {
      case e: Exception => return false;
    }
  }

  override def activateObject(p: PooledObject[Pug]): Unit = {
    val pug = p.getObject();
    if (pug.getDB() != database) {
      pug.select(database)
    }
  }

  override def passivateObject(p: PooledObject[Pug]): Unit = {
    //TODO log it is being called.
  }

  override def makeObject(): PooledObject[Pug] = {
    val pug = new Pug(host, port, connectionTimeout, soTimeout, ssl,
      sslSocketFactory, sslParameters, hostnameVerifier)

    try {
      pug.connect()
      if (this.password != null) {
        pug.auth(this.password)
      }
      if (database != 0) {
        pug.select(database)
      }
      if (clientName != null) {
        pug.clientSetname(clientName)
      }
    } catch {
      case e: JedisException => pug.close()
        throw e;
    }

    new DefaultPooledObject[Pug](pug)
  }
}
