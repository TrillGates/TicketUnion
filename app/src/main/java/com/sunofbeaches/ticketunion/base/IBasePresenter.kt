package com.sunofbeaches.ticketunion.base

/**
 * Created by TrillGates on 2019-12-17.
 * God bless my code!
 */
interface IBasePresenter<T> {
    fun registerCallback(callback: T)

    fun unregisterCallback(callback: T)
}