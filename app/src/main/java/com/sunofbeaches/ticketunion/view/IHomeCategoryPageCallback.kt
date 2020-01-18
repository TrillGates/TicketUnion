package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.base.IBaseHomePagerCallback
import com.sunofbeaches.ticketunion.model.domain.CategoryPageItem

interface IHomeCategoryPageCallback : IBaseHomePagerCallback {

    fun onContentLoaded(result: CategoryPageItem, categoryId: Int)

    fun onLoaderMoreEmpty(categoryId: Int)

    fun onLoaderMoreError(categoryId: Int)

    fun onLoaderMoreLoaded(result: CategoryPageItem, categoryId: Int)

    fun onLooperDataLoaded(looper: List<CategoryPageItem.Data>, categoryId: Int)
}