package com.sunofbeaches.ticketunion.utils

import com.sunofbeaches.ticketunion.presenter.impl.*

object PresenterManager {
    private val homePagePresenter: HomePagePresenterImpl by lazy {
        HomePagePresenterImpl()
    }

    fun getHomePagePresenterImpl(): HomePagePresenterImpl {
        return homePagePresenter
    }

    private val categoryPagePresenter by lazy {
        CategoryPagePresenterImpl()
    }

    fun getCategoryPagePresenterImpl(): CategoryPagePresenterImpl {
        return categoryPagePresenter
    }

    private val qrCodePagePresenter by lazy {
        QrCodeScanPresenterImpl()
    }

    fun getQrCodeScanPresenterImpl(): QrCodeScanPresenterImpl {
        return qrCodePagePresenter
    }
    private val onSellPagePresenter by lazy {
        OnSellPagePresenterImpl()
    }

    fun getOnSellPagePresenterImpl(): OnSellPagePresenterImpl {
        return onSellPagePresenter
    }

    private val searchPagePresenter by lazy {
        SearchPagePresenterImpl()
    }

    fun getSearchPagePresenterImpl(): SearchPagePresenterImpl {
        return searchPagePresenter
    }

    private val selectedPagePresenter by lazy {
        SelectedPagePresenterImpl()
    }

    fun getSelectedPagePresenterImpl(): SelectedPagePresenterImpl {
        return selectedPagePresenter
    }

    private val ticketPagePresenter by lazy {
        TicketPresenterImpl()
    }

    fun getTicketPresenterImpl(): TicketPresenterImpl {
        return ticketPagePresenter
    }
}