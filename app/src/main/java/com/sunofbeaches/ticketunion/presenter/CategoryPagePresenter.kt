package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.IHomeCategoryPageCallback

abstract class CategoryPagePresenter : BasePresenter<IHomeCategoryPageCallback>() {
    /**
     * 下拉刷新更多
     */
    abstract fun getCategoryContentById(categoryId: Int)

    /**
     * 加载更多内容
     */
    abstract fun loaderMore(categoryId: Int)

    /**
     * 加载更多内容
     */
    abstract fun reload(categoryId: Int)
}
