package me.ugo.svolley.tests

import android.test.InstrumentationTestCase
import android.util.Log
import com.android.volley.Request.Method
import junit.framework.Assert
import me.ugo.svolley.RequestBuilder
import org.json.JSONObject
;

/**
 * Created by knshiro on 6/25/14.
 */
class SRequestTest extends InstrumentationTestCase with LoggerTest {


  def testStringSerializer() {
    Log.i("SVOLLEY", "Hello world!")
    val body = "Hello world!"
    val rb = implicitly[RequestBuilder[String]]
    val request = rb.request(Method.POST,"",Some(body))
    Assert.assertEquals("Body should be same",body,new String(request.body))
  }

  def testJsonSerializer(){
    val body = new JSONObject
    body.put("key1", "value1")
    body.put("key2", "value2")
    val rb = implicitly[RequestBuilder[JSONObject]]
    val request = rb.request(Method.POST, "", Some(body))

    val stringBody = new String(request.body)
    info("Encoded body: " + stringBody)
    val reparsedBody = new JSONObject(stringBody)
    Assert.assertEquals("Expected value1 in key1", "value1", reparsedBody.get("key1"))
    Assert.assertEquals("Expected value1 in key2", "value2", reparsedBody.get("key2"))
  }

}
