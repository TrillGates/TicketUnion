package com.sunofbeaches.ticketunion.model.domain

data class OnSellResultItem(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val tbk_dg_optimus_material_response: TbkDgOptimusMaterialResponse
    ) {
        data class TbkDgOptimusMaterialResponse(
            val is_default: String,
            val request_id: String,
            val result_list: ResultList
        ) {
            data class ResultList(
                val map_data: List<MapData>
            ) {
                data class MapData(
                    val category_id: Int,
                    val category_name: String,
                    val click_url: String,
                    val commission_rate: String,
                    val coupon_amount: Int,
                    val coupon_click_url: String,
                    val coupon_end_time: String,
                    val coupon_info: String,
                    val coupon_remain_count: Int,
                    val coupon_share_url: String,
                    val coupon_start_fee: String,
                    val coupon_start_time: String,
                    val coupon_total_count: Int,
                    val item_description: String,
                    val item_id: Long,
                    val level_one_category_id: Int,
                    val level_one_category_name: String,
                    val nick: String,
                    val pict_url: String,
                    val seller_id: Long,
                    val shop_title: String,
                    val small_images: SmallImages,
                    val title: String,
                    val user_type: Int,
                    val volume: Int,
                    val zk_final_price: String
                ) {
                    data class SmallImages(
                        val string: List<String>
                    )
                }
            }
        }
    }
}