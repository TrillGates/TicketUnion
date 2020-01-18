package com.sunofbeaches.ticketunion.model.domain


data class SearchResult(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val tbk_dg_material_optional_response: TbkDgMaterialOptionalResponse
    ) {
        data class TbkDgMaterialOptionalResponse(
            val request_id: String,
            val result_list: ResultList,
            val total_results: Int
        ) {
            data class ResultList(
                val map_data: List<MapData>
            ) {
                data class MapData(
                    val category_id: Int,
                    val category_name: String,
                    val commission_rate: String,
                    val commission_type: String,
                    val coupon_amount: String,
                    val coupon_end_time: String,
                    val coupon_id: String,
                    val coupon_info: String,
                    val coupon_remain_count: Int,
                    val coupon_share_url: String,
                    val coupon_start_fee: String,
                    val coupon_start_time: String,
                    val coupon_total_count: Int,
                    val include_dxjh: String,
                    val include_mkt: String,
                    val info_dxjh: String,
                    val item_description: String,
                    val item_id: Long,
                    val item_url: String,
                    val jdd_num: Int,
                    val jdd_price: Any,
                    val level_one_category_id: Int,
                    val level_one_category_name: String,
                    val nick: String,
                    val num_iid: Long,
                    val oetime: Any,
                    val ostime: Any,
                    val pict_url: String,
                    val presale_deposit: Any,
                    val presale_end_time: Int,
                    val presale_start_time: Int,
                    val presale_tail_end_time: Int,
                    val presale_tail_start_time: Int,
                    val provcity: String,
                    val real_post_fee: String,
                    val reserve_price: String,
                    val seller_id: Long,
                    val shop_dsr: Int,
                    val shop_title: String,
                    val short_title: String,
                    val small_images: SmallImages,
                    val title: String,
                    val tk_total_commi: String,
                    val tk_total_sales: String,
                    val url: String,
                    val user_type: Int,
                    val volume: Int,
                    val white_image: String,
                    val x_id: String,
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
