package com.sunofbeaches.ticketunion.model.domain

data class MainCategoryItem(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val id: Int,
        val title: String
    )
}