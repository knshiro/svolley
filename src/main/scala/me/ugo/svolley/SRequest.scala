/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package me.ugo.svolley

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

import com.android.volley.Request.Method
import com.android.volley.VolleyLog
import org.json.JSONObject


trait SRequest[T] {

  def method: Int
  def url: String
  def params: Option[T]
  def headers: Map[String, String]
  def mimeType:String
  def paramsEncoding:String
  def bodyContentType: String = {
    mimeType + (if (paramsEncoding != "") ", charset=" + paramsEncoding else "")
  }
  def body: Array[Byte]
}

object SRequest {
  def urlQuery(method: Int,
            url: String,
            params: Option[Map[String, String]] = None,
            headers: Map[String, String] = Map()):SRequest[Map[String, String]] = FormUrlRequest(method,url,params,headers)

  def apply[T](method:Int, url:String, requestBody:Option[T] = None, headers:Map[String,String] = Map())(implicit rb:RequestBuilder[T]):SRequest[T] = {
    rb.request(method, url, requestBody, headers)
  }

}

case class StringRequest(
                          val method: Int,
                          val url: String,
                          val params: Option[String] = None,
                          val headers: Map[String, String] = Map(),
                          val paramsEncoding: String = "UTF-8",
                          val mimeType:String = "text/plain")

  extends SRequest[String] {

  def body: Array[Byte] = {
    params.fold(Array[Byte]()) { p =>
      var res: Array[Byte] = Array()
      try {
        res = p.toString().getBytes(paramsEncoding);
      } catch {
        case uee: UnsupportedEncodingException =>
          VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
            params, paramsEncoding)
      }
      res
    }
  }

}

case class JsonRequest(
                        val method: Int,
                        val url: String,
                        val params: Option[JSONObject] = None,
                        val headers: Map[String, String] = Map(),
                        val paramsEncoding:String = "UTF-8")
  extends SRequest[JSONObject] {

  val mimeType = "application/json"

  def body: Array[Byte] = {
    params.fold(Array[Byte]()) { p =>
      try {
        return p.toString().getBytes(paramsEncoding);
      } catch {
        case uee: UnsupportedEncodingException =>
          VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
            params, paramsEncoding)
          return Array()
      }
    }
  }
}


trait FormUrlRequest extends SRequest[Map[String, String]] {

  def paramsEncoding:String

  def encodeParams(params: Map[String, String]): String = {
    var res = ""
    val encodedParams = new StringBuilder();
    try {
      for ((key, value) <- params) {
        encodedParams.append(URLEncoder.encode(key, paramsEncoding));
        encodedParams.append('=');
        encodedParams.append(URLEncoder.encode(value, paramsEncoding));
        encodedParams.append('&');
      }
      res = encodedParams.toString()
    } catch {
      case uee: UnsupportedEncodingException =>
        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
          params, paramsEncoding)
    }
    res
  }

}


object FormUrlRequest {

  case class FormUrlBodyRequest(
                                 method: Int,
                                 url: String,
                                 params: Option[Map[String, String]] = None,
                                 headers: Map[String, String] = Map(),
                                 paramsEncoding:String = "UTF-8")
    extends FormUrlRequest {

    val mimeType = "application/x-www-form-urlencoded"

    def body: Array[Byte] = {
      params map (encodeParams(_).getBytes(paramsEncoding)) getOrElse Array()
    }
  }

  case class FormUrlQueryRequest(
                                  method: Int,
                                  _url: String,
                                  params: Option[Map[String, String]] = None,
                                  headers: Map[String, String] = Map(),
                                  paramsEncoding:String = "UTF-8")
    extends FormUrlRequest {

    val mimeType = ""
    override val bodyContentType = ""

    val url: String = params.fold(_url) { p => _url + '?' + encodeParams(p)}
    val body = Array.empty[Byte]

  }

  def apply(method: Int,
            url: String,
            params: Option[Map[String, String]] = None,
            headers: Map[String, String] = Map()): FormUrlRequest = {

    if (method == Method.GET) {
      FormUrlQueryRequest(method, url, params, headers)
    }
    else {
      FormUrlBodyRequest(method, url, params, headers)
    }

  }
}
