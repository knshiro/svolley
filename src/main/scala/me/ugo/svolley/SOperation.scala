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
import scala.language.existentials


abstract class SOperation[T](request:SRequest[_])
  extends Request[T](request.method, request.url,null)
{
  
  protected val p = promise[T]

  def future = p.future

  def getAcceptableContentTypes():List[String]
  def parseResponse(response:NetworkResponse):Response[T]

  override def getBodyContentType() = request.bodyContentType
  override def getBody() = request.body
  override def getHeaders() = {
    if (!getAcceptableContentTypes.isEmpty)
      request.headers + ("Accept" -> getAcceptableContentTypes.mkString(","))
    else
      request.headers
  }
  
  override def deliverError(error:VolleyError) { p failure error }
  override def deliverResponse(response:T) { p success response }
  override def parseNetworkResponse(response:NetworkResponse) = parseResponse(response)
}

object SOperation {
  import scala.language.implicitConversions

  type OperationBuilder[T] = SRequest[_] => SOperation[T]


  implicit def toStringOperation(request:SRequest[_]):StringOperation = StringOperation(request)
  implicit def toJsonObjectOperation(request:SRequest[_]):JsonObjectOperation = JsonObjectOperation(request)
  implicit def toJsonArrayOperation(request:SRequest[_]):JsonArrayOperation = JsonArrayOperation(request)

}

trait JsonOperation {
  def getAcceptableContentTypes():List[String] = List("application/json", "text/json", "text/javascript")
}


case class JsonObjectOperation(request:SRequest[_]) extends SOperation[JSONObject](request) with JsonOperation {

  def parseResponse(response:NetworkResponse):Response[JSONObject] = {
    try {
      val jsonString =
        new String(response.data, HttpHeaderParser.parseCharset(response.headers));
      if(jsonString.isEmpty() || jsonString == " "){
        return Response.success(new JSONObject(),
          HttpHeaderParser.parseCacheHeaders(response))
      }
      return Response.success(new JSONObject(jsonString),
        HttpHeaderParser.parseCacheHeaders(response));
    } catch {
      case e:UnsupportedEncodingException =>
        return Response.error(new ParseError(e));
      case je:JSONException =>
        return Response.error(new ParseError(je));
    }
  }
}

case class JsonArrayOperation(request:SRequest[_]) extends SOperation[JSONArray](request) with JsonOperation {

  def parseResponse(response:NetworkResponse):Response[JSONArray] =  {
    try {
      val jsonString =
        new String(response.data, HttpHeaderParser.parseCharset(response.headers))
      if(jsonString.isEmpty() || jsonString == " "){
        return Response.success(new JSONArray(),
          HttpHeaderParser.parseCacheHeaders(response))
      }
      return Response.success(new JSONArray(jsonString),
        HttpHeaderParser.parseCacheHeaders(response));
    } catch {
      case e:UnsupportedEncodingException =>
        return Response.error(new ParseError(e));
      case je:JSONException =>
        return Response.error(new ParseError(je));
    }
  }

}

case class StringOperation(request:SRequest[_]) extends SOperation[String](request) {

  def getAcceptableContentTypes():List[String] = List("*/*")

  def parseResponse(response:NetworkResponse):Response[String] = {
    var parsed:String = null
    try {
      parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    } catch {
      case e:UnsupportedEncodingException =>
        parsed = new String(response.data);
    }
    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
  }
}



