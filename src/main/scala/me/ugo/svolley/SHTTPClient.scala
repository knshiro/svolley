/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */
package me.ugo.svolley

import scala.collection.mutable.{Map => MMap}
import scala.concurrent.Future
import scala.concurrent.{ future, promise }

import com.android.volley.Request.Method
import com.android.volley.RequestQueue

import android.util.Base64

import SOperation._

object SHTTPClient {

  val DefaultHTTPMethodsEncodingParametersInURI = Set(Method.GET, Method.HEAD,Method.DELETE)

  def apply(url:String) = new SHTTPClient {
    override def baseUrl: String = url
  }

}

trait SHTTPClient {

  var HTTPMethodsEncodingParametersInURI = SHTTPClient.DefaultHTTPMethodsEncodingParametersInURI

  def baseUrl:String
  
  private val mHeaders:MMap[String,String] = MMap()
  
  def setDefaultHeader(header:String, value:String) { mHeaders += (header -> value) }
  def clearDefaultHeader(header:String) { mHeaders -= header}
  
  def setAuthorizationHeader(username:String,password:String) {
    //from https://github.com/njzk2/VolleyTwitter/blob/master/src/com/njzk2/twitterbrowser/TokenRequest.java
    val auth = s"Basic ${Base64.encodeToString((username + ":" + password).getBytes(),Base64.NO_WRAP)}"
    setDefaultHeader("Authorization",auth)
  }
  def clearAuthorizationHeader { clearDefaultHeader("Authorization") }


  def buildUrl(path:String):String = {
    if(baseUrl.last == '/') baseUrl + path
    else baseUrl + "/" + path
  }

  def buildHeaders(headers:Map[String,String]):Map[String,String] = headers ++ mHeaders


  def get[T](path:String, params:Map[String,String]=Map(), headers:Map[String,String] = Map())(implicit rq:RequestQueue, ob:OperationBuilder[T]):Future[T] = {
    val req = SRequest.urlQuery(Method.GET, buildUrl(path), Some(params), buildHeaders(headers))
    performRequest[T](req)
  }

  def delete[T](path:String, params:Map[String,String] = Map(), headers:Map[String,String] = Map())(implicit rq:RequestQueue, ob:OperationBuilder[T]):Future[T] = {
    val req = SRequest.urlQuery(Method.DELETE, buildUrl(path), Some(params), buildHeaders(headers))
    performRequest[T](req)
  }

  def post[T,U](path:String, requestBody:Option[T] = None, headers:Map[String,String] = Map())(implicit rq:RequestQueue, ev:RequestBuilder[T], ob:OperationBuilder[U]):Future[U] = {
    performRequest[T,U](Method.POST, path, requestBody, headers)
  }

  def put[T,U](path:String, requestBody:Option[T] = None, headers:Map[String,String] = Map())(implicit rq:RequestQueue, ev:RequestBuilder[T], ob:OperationBuilder[U]):Future[U] = {
    performRequest[T,U](Method.PUT, path, requestBody, headers)
  }

  def performRequest[T,U](method:Int, path:String, requestBody:Option[T] = None, headers:Map[String,String] = Map())(implicit rq:RequestQueue, rb:RequestBuilder[T], ob:OperationBuilder[U]):Future[U] = {
    val request = makeRequest(method, path, requestBody, headers)(rb,ob)
    performRequest(request)
  }

  def makeRequest[T,U](method:Int, path:String, requestBody:Option[T] = None, headers:Map[String,String] = Map())(implicit rb:RequestBuilder[T], ob:OperationBuilder[U]):SOperation[U] = {
    val req = SRequest(method, buildUrl(path), requestBody, buildHeaders(headers))
    ob(req)
  }

  def performRequest[U]( request:SOperation[U])(implicit rq:RequestQueue):Future[U] = {
    rq.add(request)
    request.future
  }


}

