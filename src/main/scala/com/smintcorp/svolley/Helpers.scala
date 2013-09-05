/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
*/
package com.smintcorp.svolley

import org.json.JSONArray
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import org.json.JSONObject

object Helpers {
  
  trait RequestBuilder[T,U] { 
    def request(method:Int, url:String, requestBody:Option[T], headers:Map[String,String] = Map()):SOperation[T,U]
  }

  object RequestBuilder {
    
    implicit object JOJORequestBuilder extends RequestBuilder[JSONObject,JSONObject] {
      def request(method:Int, url:String, requestBody:Option[JSONObject], headers:Map[String,String] = Map()) = { 
          val req = new JsonRequest(method, url, requestBody, headers)
          new JsonObjectOperation(req)
      }
    }
    
    
    implicit object JsonObjectRequestBuilder extends RequestBuilder[AnyRef,JSONObject] {
      def request(method:Int, url:String, requestBody:Option[AnyRef], headers:Map[String,String] = Map()) = { 
          val req = new StringRequest(method, url, requestBody, headers)
          new JsonObjectOperation(req)
      }
    }
    
    implicit object JsonArrayRequestBuilder extends RequestBuilder[AnyRef,JSONArray] {
      def request(method:Int, url:String, requestBody:Option[AnyRef], headers:Map[String,String] = Map()) = { 
          val req = new StringRequest(method, url, requestBody, headers)
          new JsonArrayOperation(req)
      }
    }
    
    implicit object StringRequestBuilder extends RequestBuilder[AnyRef,String] {
      def request(method:Int, url:String, requestBody:Option[AnyRef], headers:Map[String,String] = Map()) = { 
          val req = new StringRequest(method, url, requestBody, headers)
          new StringOperation(req)
      }
    }
  }
}
