/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package me.ugo.svolley

import org.json.JSONObject


abstract class SRequest[T](
    val method:Int, 
    val url:String,
    val params:Option[T] = None,
    val headers:Map[String,String] = Map()) { self: SSerializer[T] =>
  
    def getBodyContentType():String = serializer.getBodyContentType
    def getHeaders():Map[String,String] = headers
    def getBody():Array[Byte] = params map serializer.encodeBody getOrElse Array()
}

class StringRequest (
    method:Int, 
    url:String,
    params:Option[AnyRef] = None,
    headers:Map[String,String] = Map())
    extends SRequest(method,url,params,headers) with StringSerializer{
  val serializer = new StringSerializerC
}

class JsonRequest (
    method:Int, 
    url:String,
    params:Option[JSONObject] = None,
    headers:Map[String,String] = Map())
    extends SRequest(method,url,params,headers) with JsonSerializer{
  val serializer = new JsonSerializerC
}
    
class FormUrlRequest (
    method:Int, 
    url:String,
    params:Option[Map[String,String]] = None,
    headers:Map[String,String] = Map())
    extends SRequest(method,url,params,headers) with FormUrlSerializer{
    val serializer = new FormUrlSerializerC
}
