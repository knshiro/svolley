package me.ugo.svolley.tests

import android.test.AndroidTestCase
import com.android.volley.toolbox.Volley
import junit.framework.Assert
import me.ugo.svolley.SHTTPClient
import org.json.JSONObject

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Created by knshiro on 6/25/14.
 */
class SHTTPClientTest extends AndroidTestCase with LoggerTest {

  implicit lazy val rq = Volley.newRequestQueue(getContext)


  def testJsonCall(){
    val httpClient = SHTTPClient("http://ip.jsontest.com/")
    val response = Await.result(httpClient.get[JSONObject](""), 5 second)
    info("Received response: " + response.toString)
    Assert.assertNotNull(response)
  }

  def testJsonHeaders(){
    val httpClient = SHTTPClient("http://headers.jsontest.com/")
    val response = Await.result(httpClient.get[JSONObject](""), 5 second)
    val acceptHeaders = response.getString("Accept")
    info("Received response: " + response.toString)
    Assert.assertTrue(acceptHeaders.contains("application/json"))
  }

  def testJsonPost(){
    val httpClient = SHTTPClient("http://validate.jsontest.com/")
    val body = new JSONObject
    body.put("key1", "value1")
    body.put("key2", "value2")
    val response = Await.result(httpClient.post[String,JSONObject]("?json="+body.toString, Some("json="+body.toString)), 5 second)
    info("Received response: " + response.toString)
    Assert.assertEquals("Expected object type", "object", response.getString("object_or_array"))
    Assert.assertFalse("Expected non empty", response.getBoolean("empty"))
  }

}
