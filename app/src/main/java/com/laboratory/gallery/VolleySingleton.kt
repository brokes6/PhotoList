package com.laboratory.gallery

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context) {
    //单例模式
    companion object {
        //创建私有变量，让外部无法调用
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            //如果为空，则创建，创建的时候先保证线程安全
            INSTANCE ?: synchronized(this) {
                //调用内部构造方法
                VolleySingleton(context).also { INSTANCE = it }
            }
        }


    //创建一个工作池，context为缓存目录
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}