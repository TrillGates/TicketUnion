package com.sunofbeaches.ticketunion.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.model.domain.CategoryPageItem

class HomeContentListAdapter : RecyclerView.Adapter<HomeContentListAdapter.InnerHolder>() {
    private val contentList = ArrayList<CategoryPageItem.Data>()

    private var mItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_content_list, parent, false)

        return InnerHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        //绑定数据
        val itemData = contentList[position]
        holder.bindNormalContent(itemData)
        //set up item click listener.
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(contentList[position], position)
        }
    }

    fun updateData(contentList: List<CategoryPageItem.Data>) {
        val oldLength = this.contentList.size
        this.contentList.addAll(contentList)
        notifyItemRangeChanged(oldLength - 1, contentList.size)
    }

    fun setData(contentList: List<CategoryPageItem.Data>) {
        this.contentList.clear()
        this.contentList.addAll(contentList)
        notifyDataSetChanged()
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindNormalContent(itemData: CategoryPageItem.Data) {
            itemView.findViewById<TextView>(R.id.goods_title).text = itemData.title
            val cover = itemView.findViewById<ImageView>(R.id.goods_cover)
            val size = cover.layoutParams.width / 2
            val url =
                "https:${itemData.pict_url}_${size}x${size}.jpg"
            //LogUtils.d(this, "layoutParams -- > $url")
            // LogUtils.d(this, "height -- > ${cover.layoutParams.height}")
            Glide.with(itemView.context).load(url)
                .override(cover.layoutParams.width, cover.layoutParams.height).into(cover)
            val offPriseTv = itemView.findViewById<TextView>(R.id.off_prise)
            val offPrise = ((itemData.zk_final_price.toFloat()) - itemData.coupon_amount)
            offPriseTv.text = "券后价：${String.format("%.2f", offPrise)}"
            val originPriseTv = itemView.findViewById<TextView>(R.id.origin_prise)
            originPriseTv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            originPriseTv.text = itemData.zk_final_price
            val offAmountTv = itemView.findViewById<TextView>(R.id.off_amount)
            offAmountTv.text = "省${itemData.coupon_amount}元"
            val boughtCountTv = itemView.findViewById<TextView>(R.id.bought_count)
            boughtCountTv.text = "${itemData.volume}·"

        }

    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    fun cleanData() {
        this.contentList.clear()
    }

    interface OnItemClickListener {
        fun onItemClick(item: CategoryPageItem.Data, index: Int)
    }
}