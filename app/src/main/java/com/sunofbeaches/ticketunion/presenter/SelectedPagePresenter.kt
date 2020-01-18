package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.ISelectedPageCallback

abstract class SelectedPagePresenter : BasePresenter<ISelectedPageCallback>() {

    abstract fun loadPickType()

    abstract fun loadPickContentById(categoryId: Int)



}