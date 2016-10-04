package com.mine.pug

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by mine123 on 03/10/2016.
  */
trait AsyncPug {
  def aset(key: String, value: String)(implicit ec: ExecutionContext): Future[String]

  def aget(key: String)(implicit ec: ExecutionContext): Future[String]
}
