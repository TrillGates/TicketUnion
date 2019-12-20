package com.sunofbeaches.ticketunion.base

import android.view.View

interface IBaseHolder<T> {
    fun setData(data: T)

    fun getView(): View
}