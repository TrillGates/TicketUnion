package com.sunofbeaches.ticketunion.presenter

interface TicketPresenter {
    /**
     * 获取淘口令
     */
     fun loadTicketByItem(url: String, title: String, cover: String?)

}