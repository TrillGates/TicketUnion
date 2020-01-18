package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.ISearchPageCallback

abstract class SearchPagerPresenter : BasePresenter<ISearchPageCallback>() {
    abstract fun doSearch(keyword: String)

    abstract fun getHistories()

    abstract fun loaderMore()

    abstract fun getRecommendWords()
}