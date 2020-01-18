package com.sunofbeaches.ticketunion.presenter

interface ICategoryPagePresenter  {
    /**
     * 下拉刷新更多
     */
    fun getCategoryContentById(categoryId: Int)

    /**
     * 加载更多内容
     */
    fun loaderMore(categoryId: Int)

    /**
     * 加载更多内容
     */
    fun reload(categoryId: Int)
}
