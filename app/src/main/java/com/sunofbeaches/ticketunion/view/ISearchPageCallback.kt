package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.base.IBaseViewCallback
import com.sunofbeaches.ticketunion.model.domain.HistoryItem
import com.sunofbeaches.ticketunion.model.domain.SearchRecommend
import com.sunofbeaches.ticketunion.model.domain.SearchResult

interface ISearchPageCallback : IBaseViewCallback {

    fun onSearchResultLoaded(result: SearchResult)

    fun onLoaderMote(result: SearchResult)

    fun onHistoriesLoaded(histories: HistoryItem?)

    fun onLoaderMoreError()

    fun onLoaderEmpty()

    fun onRecommenWordsLoaded(recommendWord: SearchRecommend)

    fun onRecommendWordLoadError()
}