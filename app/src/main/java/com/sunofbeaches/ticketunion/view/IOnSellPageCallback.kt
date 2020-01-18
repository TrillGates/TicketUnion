package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.base.IBaseViewCallback
import com.sunofbeaches.ticketunion.model.domain.OnSellResultItem

interface IOnSellPageCallback : IBaseViewCallback {
    fun onSellContentLoaded(result: OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList)

    /**
     * 加载更多的结果
     */
    fun onLoaderMoreResult(result: OnSellResultItem)

    /**
     * 加载更多失败，提示用户稍后充实，不能切换到网络错误的界面，要不原来的数据就不了，用户懵逼。
     */
    fun onLoaderMoreError()

    /**
     * 没有更多数据了，给出提示，并且禁止加载更多的UI功能。
     */
    fun onLoaderMoreEmpty()
}