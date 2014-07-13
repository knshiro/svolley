# SVolley: A Scala wrapper around Google Volley for Android 

## Build

    sbt package

This will produce a jar in the target/scala_2.10 folder that you can add manually in your project libs folder.

Or you can use

    sbt publish-local

And add this line to your build.sbt 

    libraryDependencies += "me.ugo" %% "svolley" % "0.0.3-SNAPSHOT"


## How to use

```scala
// Imports
import com.android.volley.toolbox.Volley
import me.ugo.svolley.SHTTPClient

// Create Volley request queue, if it is created as an Activity parameter, better use lazy since context may be null at initialization
implicit lazy val rq = Volley.newRequestQueue(context)

// Create a client for your api
val httpClient = SHTTPClient("http://ip.jsontest.com/")

// Perform some requests
httpClient.get[JSONObject]("test")
```
