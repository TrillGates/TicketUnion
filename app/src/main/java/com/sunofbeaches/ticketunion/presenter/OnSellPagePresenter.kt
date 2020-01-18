package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.IOnSellPageCallback

abstract class OnSellPagePresenter : BasePresenter<IOnSellPageCallback>() {

    abstract fun loadOnSellContent()

    abstract fun loaderMore()
}