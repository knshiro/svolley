/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */
package com.smintcorp.svolley

import scala.collection.mutable.{Map => MMap}
import scala.concurrent.Future
import scala.concurrent.{ future, promise }

import com.android.volley.Request.Method
import com.android.volley.RequestQueue

import android.util.Base64
import com.smintcorp.svolley.Helpers._


trait SHTTPClient {
  
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
  
  

  def makeRequest[T,U](method:Int, path:String, requestBody:Option[T], headers:Map[String,String] = Map())(implicit ev:RequestBuilder[T,U]):SOperation[T,U] = {
    //TODO make a more robust path construction
    ev.request(method, baseUrl + "/" + path, requestBody, headers ++ mHeaders)
  }
  
  def performRequest[T,U]( request:SOperation[T,U])(implicit rq:RequestQueue) = {
    rq.add(request)
    request.future
  }
  
  def get[T](path:String, headers:Map[String,String] = Map())(implicit ev:RequestBuilder[AnyRef,T], rq:RequestQueue):Future[T] = {
    val request = makeRequest[AnyRef,T](Method.GET, path, None, headers)
    performRequest[AnyRef,T](request)
  }
  
  def post[T,U](path:String, requestBody:T, headers:Map[String,String] = Map())(implicit ev:RequestBuilder[T,U], rq:RequestQueue):Future[U] = {
    val request = makeRequest[T,U](Method.POST, path, Some(requestBody), headers)
    performRequest(request)
  }
  
  def put[T,U](path:String, requestBody:T, headers:Map[String,String] = Map())(implicit ev:RequestBuilder[T,U], rq:RequestQueue):Future[U] = {
    val request = makeRequest[T,U](Method.PUT, path, Some(requestBody), headers)
    performRequest(request)
  }
  
  def delete[T,U](path:String, requestBody:T, headers:Map[String,String] = Map())(implicit ev:RequestBuilder[T,U], rq:RequestQueue):Future[U] = {
    val request = makeRequest[T,U](Method.DELETE, path, Some(requestBody), headers)
    performRequest(request)
  }
  
}

