package com.sunofbeaches.ticketunion.model.domain

data class TickerResult(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val tbk_tpwd_create_response: TbkTpwdCreateResponse
    ) {
        data class TbkTpwdCreateResponse(
            val `data`: Data,
            val request_id: String
        ) {
            data class Data(
                val model: String
            )
        }
    }
}