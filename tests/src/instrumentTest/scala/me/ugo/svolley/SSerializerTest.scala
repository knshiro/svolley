package me.ugo.svolley

import android.test.InstrumentationTestCase
import android.util.Log
import junit.framework.Assert
;

/**
 * Created by knshiro on 6/25/14.
 */
class SSerializerTest extends InstrumentationTestCase {

  lazy val stringSerializer = new StringSerializer {
    val serializer = new StringSerializerC
  }

  def testStringSerializer() {
    Log.i("SVOLLEY", "Hello world!")
    val body = "Hello world!"
    val encodedBody = stringSerializer.serializer.encodeBody(body)
    Assert.assertEquals("Body should be same",body,new String(encodedBody))
  }

}
