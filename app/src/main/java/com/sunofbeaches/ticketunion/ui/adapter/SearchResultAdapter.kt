package com.sunofbeaches.ticketunion.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.model.domain.SearchResult
import com.sunofbeaches.ticketunion.utils.LogUtils

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.InnerHolder>() {

    private val resultList: ArrayList<SearchResult.Data.TbkDgMaterialOptionalResponse.ResultList.MapData> =
        ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_content_list, parent, false)
        val holder = InnerHolder(itemView)
        ButterKnife.bind(holder, itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        //绑定数据
        holder.bindNormalContent(resultList[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.onSearchItemClick(resultList[position])
        }
    }

    fun setData(result: SearchResult) {
        LogUtils.d(
            this,
            "setData -- > ${result.data.tbk_dg_material_optional_response.result_list.map_data.size}"
        )
        resultList.clear()
        resultList.addAll(result.data.tbk_dg_material_optional_response.result_list.map_data)
        notifyDataSetChanged()
    }

    fun updateData(result: SearchResult) {
        val originSize = resultList.size
        //加载更多的数据
        val newData = result.data.tbk_dg_material_optional_response.result_list.map_data
        resultList.addAll(newData)
        notifyItemRangeChanged(originSize - 1, newData.size)
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @BindView(R.id.goods_title)
        lateinit var title: TextView
        @BindView(R.id.goods_cover)
        lateinit var cover: ImageView
        @BindView(R.id.off_prise)
        lateinit var offPriseTv: TextView
        @BindView(R.id.origin_prise)
        lateinit var originPriseTv: TextView
        @BindView(R.id.off_amount)
        lateinit var offAmountTv: TextView
        @BindView(R.id.bought_count)
        lateinit var boughtCountTv: TextView


        fun bindNormalContent(itemData: SearchResult.Data.TbkDgMaterialOptionalResponse.ResultList.MapData) {
            title.text = itemData.title

            val size = cover.layoutParams.width

            val url = "${itemData.pict_url}_${size}x${size}.jpg"
            LogUtils.d(this, "url -- > $url")
            // LogUtils.d(this, "height -- > ${cover.layoutParams.height}")
            Glide.with(itemView.context).load(url)
                .override(cover.layoutParams.width, cover.layoutParams.height).into(cover)
            if (itemData.coupon_amount != null && itemData.zk_final_price != null) {
                //节省的钱
                val offPrise =
                    ((itemData.zk_final_price.toFloat()) - itemData.coupon_amount.toFloat())
                offPriseTv.visibility = View.VISIBLE
                offPriseTv.text = "券后价：${String.format("%.2f", offPrise)}"
                offAmountTv.visibility = View.VISIBLE
                offAmountTv.text = "省${itemData.coupon_amount}元"
                originPriseTv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                offPriseTv.visibility = View.GONE
                offAmountTv.visibility = View.GONE
                originPriseTv.paint.flags = 0
            }
            originPriseTv.text = itemData.zk_final_price
            boughtCountTv.text = "${itemData.volume}·"
        }
    }


    private var itemClickListener: OnSearchItemClickListener? = null

    fun setOnSearchItemClickListener(listener: OnSearchItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnSearchItemClickListener {
        fun onSearchItemClick(item: SearchResult.Data.TbkDgMaterialOptionalResponse.ResultList.MapData)
    }

}