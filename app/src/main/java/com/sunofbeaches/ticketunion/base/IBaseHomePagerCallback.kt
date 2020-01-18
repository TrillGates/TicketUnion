package com.sunofbeaches.ticketunion.base

interface IBaseHomePagerCallback {
    /**
     *  加载中
     */
    fun onLoading(key: Any)

    /**
     * 网络错误
     */
    fun onError(key: Any)

    /**
     * 内容为空
     */
    fun onEmpty(key: Any)
}