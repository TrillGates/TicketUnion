package com.sunofbeaches.ticketunion.base

interface IBaseViewCallback {
    /**
     *  加载中
     */
    fun onLoading()

    /**
     * 网络错误
     */
    fun onError()

    /**
     * 内容为空
     */
    fun onEmpty()
}