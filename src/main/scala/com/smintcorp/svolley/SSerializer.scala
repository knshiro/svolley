/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.smintcorp.svolley

import com.android.volley.VolleyLog
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import org.json.JSONObject

/**
 * Http body serializer
 * provide ways to serialize objects to byte arrays with encodeBody method
 */
trait SSerializer[T] {

  val serializer:SSerializerC[T]

  trait SSerializerC[T]{
    def getStringEncoding():String
    def getBodyContentType():String
    def encodeBody(params:T):Array[Byte] 
  }
}

trait FormUrlSerializer extends SSerializer[Map[String,String]] {
  
    val serializer:FormUrlSerializerC
  
    val paramsEncoding = "UTF-8"
    val bodyContentType = "application/x-www-form-urlencoded; charset=" + paramsEncoding
    
    class FormUrlSerializerC extends SSerializerC[Map[String,String]]{
      def getStringEncoding() = paramsEncoding
      def getBodyContentType() = bodyContentType

      /**
       * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
       */
      def encodeBody(params:Map[String,String]):Array[Byte] = {
        val encodedParams = new StringBuilder();
        try {
          for ( (key,value) <- params) {
            encodedParams.append(URLEncoder.encode(key, paramsEncoding));
            encodedParams.append('=');
            encodedParams.append(URLEncoder.encode(value, paramsEncoding));
            encodedParams.append('&');
          }
          return encodedParams.toString().getBytes(paramsEncoding);
        } catch {
          case uee:UnsupportedEncodingException =>
          VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
              params, paramsEncoding)
              return Array()
        }
      }
    }
  
}

trait StringSerializer extends SSerializer[AnyRef] {
  
    val serializer:StringSerializerC

    val paramsEncoding = "UTF-8"
    val bodyContentType = "application/x-www-form-urlencoded; charset=" + paramsEncoding
    
    class StringSerializerC extends SSerializerC[AnyRef]{

      def getStringEncoding() = paramsEncoding
      def getBodyContentType() = bodyContentType
  
       /**
       * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
       */
      def encodeBody(params:AnyRef):Array[Byte] = {
          try {
              return params.toString().getBytes(paramsEncoding);
          } catch {
            case uee:UnsupportedEncodingException =>
              VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                      params, paramsEncoding)
              return Array()
          }
      }
    }
  
}

trait JsonSerializer extends SSerializer[JSONObject] {
  
    val serializer:JsonSerializerC

    val paramsEncoding = "UTF-8"
    val bodyContentType = s"application/json; charset=${paramsEncoding}";
    
    class JsonSerializerC extends SSerializerC[JSONObject]{

      def getStringEncoding() = paramsEncoding
      def getBodyContentType() = bodyContentType
  
       /**
       * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
       */
      def encodeBody(params:JSONObject):Array[Byte] = {
          try {
              return params.toString().getBytes(paramsEncoding);
          } catch {
            case uee:UnsupportedEncodingException =>
              VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                      params, paramsEncoding)
              return Array()
          }
      }
    }
}
