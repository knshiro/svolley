/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */  

package com.smintcorp.svolley

import java.io.UnsupportedEncodingException
import com.android.volley.{Response,NetworkResponse,ParseError}
import com.android.volley.toolbox.HttpHeaderParser
import org.json.{JSONArray,JSONObject,JSONException}

trait SParser[T] {
  def getAcceptableContentTypes():List[String] = List()
  def parseResponse(response:NetworkResponse):Response[T]
}

trait StringParser extends SParser[String] {
  
  override def getAcceptableContentTypes():List[String] = List("*/*")

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

trait JsonParser { self:SParser[_] =>
    override def getAcceptableContentTypes():List[String] = List("application/json", "text/json", "text/javascript")
}

trait JsonObjectParser extends SParser[JSONObject] with JsonParser { 

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

trait JsonArrayParser extends SParser[JSONArray] with JsonParser { 

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
