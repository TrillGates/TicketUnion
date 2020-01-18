package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.IHomePageCallback

/**
 * Created by TrillGates on 2019-12-17.
 * God bless my code!
 */
abstract class HomePagePresenter : BasePresenter<IHomePageCallback>() {
    //加载默认数据
    abstract fun loadCategories()
}