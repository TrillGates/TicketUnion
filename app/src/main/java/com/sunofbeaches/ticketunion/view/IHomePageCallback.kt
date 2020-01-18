package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.base.IBaseViewCallback
import com.sunofbeaches.ticketunion.model.domain.MainCategoryItem

interface IHomePageCallback : IBaseViewCallback {
    /**
     * 分类加载出来了
     */
    fun onCategoriesLoaded(result: MainCategoryItem?)


}