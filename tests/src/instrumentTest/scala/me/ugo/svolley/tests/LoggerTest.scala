package me.ugo.svolley.tests

import android.util.Log

/**
 * Created by knshiro on 7/13/14.
 */
trait LoggerTest {
  val TAG="SVOLLEY-TEST"

  def info(msg:String) = Log.i(TAG,msg)
  def info(msg:String,tr:Throwable) = Log.i(TAG,msg,tr)
  def debug(msg:String) = Log.d(TAG,msg)
  def warn(msg:String) = Log.w(TAG,msg)
  def warn(msg:String,tr:Throwable) = Log.w(TAG,msg,tr)
  def error(msg:String) = Log.e(TAG,msg)
  def error(msg:String,tr:Throwable) = Log.e(TAG,msg,tr)

}
