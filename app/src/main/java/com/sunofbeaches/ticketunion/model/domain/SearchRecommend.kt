package com.sunofbeaches.ticketunion.model.domain

data class SearchRecommend(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(


        val createTime: String,
        val id: String,
        val keyword: String
    ) {
        override fun toString(): String {
            return "Data(createTime='$createTime', id='$id', keyword='$keyword')"
        }
    }
}