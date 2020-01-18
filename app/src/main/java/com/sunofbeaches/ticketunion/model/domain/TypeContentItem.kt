package com.sunofbeaches.ticketunion.model.domain

data class TypeContentItem(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val tbk_uatm_favorites_item_get_response: TbkUatmFavoritesItemGetResponse
    ) {
        data class TbkUatmFavoritesItemGetResponse(
            val request_id: String,
            val results: Results,
            val total_results: Int
        ) {
            data class Results(
                val favoriteId: Int,
                val uatm_tbk_item: List<UatmTbkItem>
            ) {
                data class UatmTbkItem(
                    val click_url: String,
                    val coupon_click_url: String,
                    val coupon_end_time: String,
                    val coupon_info: String,
                    val coupon_remain_count: Int,
                    val coupon_start_time: String,
                    val coupon_total_count: Int,
                    val event_end_time: String,
                    val event_start_time: String,
                    val item_url: String,
                    val num_iid: Long,
                    val pict_url: String,
                    val reserve_price: String,
                    val status: Int,
                    val title: String,
                    val tk_rate: String,
                    val type: Int,
                    val user_type: Int,
                    val volume: Int,
                    val zk_final_price: String,
                    val zk_final_price_wap: String
                )
            }
        }
    }
}