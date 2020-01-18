package com.sunofbeaches.ticketunion.model.domain

data class PickTypeItem(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val favorites_id: Int,
        val favorites_title: String,
        val type: Int
    )
}