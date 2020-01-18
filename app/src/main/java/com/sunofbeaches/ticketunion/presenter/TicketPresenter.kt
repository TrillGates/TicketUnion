package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.ITicketCallback

abstract class TicketPresenter : BasePresenter<ITicketCallback>() {
    /**
     * 获取淘口令
     */
    abstract fun loadTicketByItem(url: String, title: String, cover: String?)

}