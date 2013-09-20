# SVolley: A Scala wrapper around Google Volley for Android 

## Build

    sbt package

This will produce a jar in the target/scala_2.10 folder.

If you try to use sbt publish-local the generated ivy contains a dependency for volley-0.1-SNAPSHOT that does not exists so you have to remove it by hand.

## Project dependency

I didn't integrate volley in the build so you need to add it to your project either by compiling it by hand or using [android-sdk-plugin](https://github.com/pfn/android-sdk-plugin).

## How to use

### Import 

    import com.smintcorp.svolley.SHTTPClient
    import com.smintcorp.svolley.Helpers._

### Create an implicit Volley request queue

    implicit lazy val rq = Volley.newRequestQueue(context)

### Create a client

    val httpClient = new SHTTPClient { val baseURl = "http://myapi.com" }

### Perform some requests
   
    httpClient.get[JSONObject]("test")

