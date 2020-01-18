package com.sunofbeaches.ticketunion.utils

import android.graphics.Point
import android.view.View

object UrlUtils {
    fun getDiscoveryContentUrl(categoryId: Int, page: Int): String {
        return "discovery/$categoryId/$page"
    }

    fun getOnSellUrl(defaultPage: Int): String {
        return "onSell/$defaultPage"
    }

    fun getTypContentUrl(categoryId: Int): String {
        return "recommend/$categoryId"
    }


}