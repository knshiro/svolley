/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */
package me.ugo.svolley

import scala.collection.JavaConversions.mapAsJavaMap
import scala.concurrent.promise
import com.android.volley._
import com.android.volley.toolbox._
import java.io.UnsupportedEncodingException
import org.json.{JSONArray,JSONObject,JSONException}


abstract class SOperation[T,U](request:SRequest[T]) 
  extends Request[U](request.method, request.url,null)
{ self:SParser[U] =>
  
  protected val p = promise[U]

  def future = p.future
   
  override def getBodyContentType() = request.getBodyContentType
  override def getBody() = request.getBody
  override def getHeaders() = {
    if (!getAcceptableContentTypes.isEmpty)
      request.headers + ("Accept" -> getAcceptableContentTypes.mkString(","))
    else
      request.headers
  }
  
  override def deliverError(error:VolleyError) { p failure error }
  override def deliverResponse(response:U) { p success response }
  override def parseNetworkResponse(response:NetworkResponse) = parseResponse(response)
}

class JsonObjectOperation[T](request:SRequest[T]) extends SOperation[T,JSONObject](request) with JsonObjectParser

class JsonArrayOperation[T](request:SRequest[T]) extends SOperation[T,JSONArray](request) with JsonArrayParser

class StringOperation[T](request:SRequest[T]) extends SOperation[T,String](request) with StringParser



