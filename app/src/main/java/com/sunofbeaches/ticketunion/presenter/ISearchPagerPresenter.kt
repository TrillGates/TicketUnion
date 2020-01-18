package com.sunofbeaches.ticketunion.presenter

interface ISearchPagerPresenter {
     fun doSearch(keyword: String)

     fun getHistories()

     fun loaderMore()

     fun getRecommendWords()
}