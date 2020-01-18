package com.sunofbeaches.ticketunion.base

import android.app.Application
import android.content.Context
import com.vondear.rxtool.RxTool

/**
 * Created by TrillGates on 2019-12-10.
 * God bless my code!
 */
class BaseApplication : Application() {

    companion object {
        var appContext: Context? = null
        fun getContext(): Context {
            return appContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = baseContext
        RxTool.init(this)
    }
}