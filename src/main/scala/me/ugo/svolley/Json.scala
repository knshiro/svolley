/*
 * Copyright 2013 Ugo Bataillard ugo@bataillard.me
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */
package me.ugo.svolley

import org.json.JSONArray
import scala.language.implicitConversions
import scala.util.Try

object JsonConversions {
  
  implicit def JSONArrayToSeq(array:JSONArray):Seq[AnyRef] = {
     var i = 0
     val objs =  for (i <- 0 until array.length()) yield {
        Option(array.opt(i))
      } 
     objs.toList.flatten
  } 
}
