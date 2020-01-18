package com.sunofbeaches.ticketunion.base

import com.sunofbeaches.ticketunion.utils.RetrofitManager

/**
 * Created by TrillGates on 2019-12-17.
 * God bless my code!
 */
open class BasePresenter<C> {

    protected val callbacks: ArrayList<C> = ArrayList()

    open fun registerCallback(callback: C) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }

    open fun unregisterCallback(callback: C) {
        callbacks.remove(callback)
    }


    protected val mApi by lazy {
        RetrofitManager.instance?.getApi()
    }

    open fun reload() {

    }
}