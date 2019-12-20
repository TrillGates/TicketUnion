package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.domain.ContentItem
import com.sunofbeaches.ticketunion.domain.MainCategoryItem

interface IHomeCallback {
    /**
     * 分类加载出来了
     */
    fun onCategoriesLoaded(result: MainCategoryItem?)

    /**
     * 轮播图内容加载了
     */
    fun onLooperLoaded(looper: List<ContentItem.Data>)

    /**
     * 列表数据加载了
     */
    fun onContentListUploaded(contentList: List<ContentItem.Data>)
}