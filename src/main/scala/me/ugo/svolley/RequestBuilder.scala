package me.ugo.svolley

import org.json.JSONObject

/**
 * Created by knshiro on 7/9/14.
 */
trait RequestBuilder[T] {
  def request(method:Int, url:String, requestBody:Option[T] = None, headers:Map[String,String] = Map()):SRequest[T]
}

object RequestBuilder {

  implicit object JsonObjectRequestBuilder extends RequestBuilder[JSONObject] {
    def request(method:Int, url:String, requestBody:Option[JSONObject], headers:Map[String,String]) = {
      JsonRequest(method, url, requestBody, headers)
    }
  }

  implicit object FormUrlRequestBuilder extends RequestBuilder[Map[String,String]] {
    def request(method:Int, url:String, requestBody:Option[Map[String,String]], headers:Map[String,String]) = {
      FormUrlRequest(method, url, requestBody, headers)
    }
  }

  implicit object StringRequestBuilder extends RequestBuilder[String] {
    def request(method:Int, url:String, requestBody:Option[String], headers:Map[String,String] = Map()) = {
      StringRequest(method, url, requestBody, headers)
    }
  }
}