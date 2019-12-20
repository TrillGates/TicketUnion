package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.IBasePresenter
import com.sunofbeaches.ticketunion.view.IHomeCallback

/**
 * Created by TrillGates on 2019-12-17.
 * God bless my code!
 */
interface IHomePresenter : IBasePresenter<IHomeCallback> {
    //加载默认数据
    fun loadDefaultData()

    //加载更多数据
    fun loadMoreData()
}