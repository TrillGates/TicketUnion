package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.base.IBaseViewCallback
import com.sunofbeaches.ticketunion.model.domain.PickContentItem
import com.sunofbeaches.ticketunion.model.domain.PickTypeItem

interface ISelectedPageCallback : IBaseViewCallback {

    fun onPickTypeLoaded(pickTypeItem: PickTypeItem)

    fun onContentListLoaded(pickContentItem: PickContentItem)

}