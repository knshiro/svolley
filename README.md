# SVolley: A Scala wrapper around Google Volley for Android 

## Build

Get volley

    git submodule update --init

Build it and add it to the volley lib folder
   
    sh buildVolley.sh

Add your android.jar in the lib folder

And package with sbt

    sbt package

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

