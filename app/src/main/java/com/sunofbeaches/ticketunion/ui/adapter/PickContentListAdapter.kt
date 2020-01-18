package com.sunofbeaches.ticketunion.ui.adapter

import android.text.TextUtils
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
import com.sunofbeaches.ticketunion.model.domain.PickContentItem

class PickContentListAdapter : RecyclerView.Adapter<PickContentListAdapter.InnerHolder>() {

    private val content: ArrayList<PickContentItem.Data.TbkUatmFavoritesItemGetResponse.Results.UatmTbkItem> =
        ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pick_list, parent, false)

        val holder = InnerHolder(itemView)
        ButterKnife.bind(holder, itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.setData(content[position])
        //设置点击事件
        holder.itemView.setOnClickListener {
            listener?.onPickItemClick(content[position])
        }
        holder.itemView.findViewById<View>(R.id.pick_item_buy_btn).setOnClickListener {
            listener?.onPickItemClick(content[position])
        }
    }

    fun setData(pickContentItem: PickContentItem) {
        content.clear()
        content.addAll(pickContentItem.data.tbk_uatm_favorites_item_get_response.results.uatm_tbk_item)
        notifyDataSetChanged()
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.pick_item_cover)
        public lateinit var cover: ImageView
        @BindView(R.id.pick_origin_prise)
        public lateinit var originPrise: TextView

        @BindView(R.id.pick_item_buy_btn)
        public lateinit var buyBtn: View

        @BindView(R.id.pick_off_prise)
        public lateinit var offPirseTv: TextView

        fun setData(uatmTbkItem: PickContentItem.Data.TbkUatmFavoritesItemGetResponse.Results.UatmTbkItem) {
            val size = cover.layoutParams.height / 2
            val url = "${uatmTbkItem.pict_url}_${size}x${size}.jpg"
            Glide.with(itemView.context).load(url)
                .placeholder(R.mipmap.placehoder_pic).into(cover)
            if (TextUtils.isEmpty(uatmTbkItem.coupon_click_url)) {
                originPrise.text = "晚啦，没有优惠券了"
                buyBtn.visibility = View.GONE
            } else {
                buyBtn.visibility = View.VISIBLE
                originPrise.text = "原价：${uatmTbkItem.zk_final_price}"
            }
            if (TextUtils.isEmpty(uatmTbkItem.coupon_info)) {
                offPirseTv.visibility = View.GONE
            } else {
                offPirseTv.visibility = View.VISIBLE
                offPirseTv.text = uatmTbkItem.coupon_info
            }
        }
    }

    private var listener: OnPickerListItemClickListener? = null

    fun setOnPickerListItemClickListener(listener: OnPickerListItemClickListener) {
        this.listener = listener
    }

    interface OnPickerListItemClickListener {
        fun onPickItemClick(item: PickContentItem.Data.TbkUatmFavoritesItemGetResponse.Results.UatmTbkItem)
    }
}