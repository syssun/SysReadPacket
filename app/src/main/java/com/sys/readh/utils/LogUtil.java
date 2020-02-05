package com.sys.readh.utils;

import android.util.Log;

/**
 * @des 日志
 * @author sys
 */
public class LogUtil {
    private static final boolean flag = true ; //日志开关
    private static final String myTag = "sysapp" ;
    private static final String myETag = "esysapp" ;
    public static void d(String tag, String message){
        if(flag){
            Log.d(tag, message);
        }
    }
    public static void e(String tag,String message, Throwable e){
        if(flag){
            Log.e(myTag,message,e);
        }
    }
    public static void d(String message){
        d(myTag, message);
    }
    public static void e(String message,Throwable e){
        e(myETag, message,e);
    }
}
