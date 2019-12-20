package com.sunofbeaches.ticketunion.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.domain.ContentItem

class HomeContentListAdapter : RecyclerView.Adapter<HomeContentListAdapter.InnerHolder>() {
    private val contentList = ArrayList<ContentItem.Data>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_content_list, parent, false)
        return InnerHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemData = contentList[position]
        holder.bindContent(itemData)
    }

    fun setData(contentList: List<ContentItem.Data>) {
        this.contentList.clear()
        this.contentList.addAll(contentList)
        notifyDataSetChanged()
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindContent(itemData: ContentItem.Data) {
            itemView.findViewById<TextView>(R.id.goods_title).text = itemData.title
            val url = "http:${itemData.pict_url}"
            val cover = itemView.findViewById<ImageView>(R.id.goods_cover)
            Glide.with(itemView.context).load(url).into(cover)
            val offPriseTv = itemView.findViewById<TextView>(R.id.off_prise)
            val offPrise = ((itemData.zk_final_price.toFloat()) - itemData.coupon_amount)
            offPriseTv.text = String.format("%.2f", offPrise)
            val originPriseTv = itemView.findViewById<TextView>(R.id.origin_prise)
            originPriseTv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            originPriseTv.text = itemData.zk_final_price
            val offAmountTv = itemView.findViewById<TextView>(R.id.off_amount)
            offAmountTv.text = "省${itemData.coupon_amount}元"

            val boughtCountTv = itemView.findViewById<TextView>(R.id.bought_count)
            boughtCountTv.text = "${itemData.volume}·"

        }
    }
}