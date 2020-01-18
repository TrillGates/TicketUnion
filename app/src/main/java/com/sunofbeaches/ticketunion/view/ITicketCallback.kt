package com.sunofbeaches.ticketunion.view

import com.sunofbeaches.ticketunion.base.IBaseViewCallback
import com.sunofbeaches.ticketunion.model.domain.TickerResult

interface ITicketCallback : IBaseViewCallback {

    //结果
    fun onTicketLoaded(result: TickerResult, cover: String?)

}