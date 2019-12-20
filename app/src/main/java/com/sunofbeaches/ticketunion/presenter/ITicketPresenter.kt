package com.sunofbeaches.ticketunion.presenter

import com.sunofbeaches.ticketunion.base.IBasePresenter
import com.sunofbeaches.ticketunion.view.ITicketCallback

interface ITicketPresenter : IBasePresenter<ITicketCallback> {
    fun loadTicket(url: String)
}