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
import com.sunofbeaches.ticketunion.model.domain.OnSellResultItem

class OnSellListAdapter : RecyclerView.Adapter<OnSellListAdapter.InnerHolder>() {

    private val contentList: ArrayList<OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList.MapData> =
        ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_on_sell_list, parent, false)
        val holder = InnerHolder(itemView)
        ButterKnife.bind(holder, itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.bindData(contentList[position])
        holder.itemView.setOnClickListener {
            listener?.onSellItemClick(contentList[position])
        }
    }

    fun setData(result: OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList) {
        this.contentList.clear()
        this.contentList.addAll(result.map_data)
        notifyDataSetChanged()
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.on_sell_goods_cover)
        public lateinit var cover: ImageView
        @BindView(R.id.on_sell_list_title)
        public lateinit var title: TextView
        @BindView(R.id.on_sell_origin_prise)
        public lateinit var originPrise: TextView
        @BindView(R.id.on_sell_result_prise)
        public lateinit var resultPrise: TextView


        fun bindData(mapData: OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList.MapData) {
            val imageSize = cover.layoutParams.height / 2
            val url = "https://${mapData.pict_url}_${imageSize}x${imageSize}.jpg"
           // LogUtils.d(this, "url -- > $url")
            Glide.with(itemView.context)
                .load(url).into(cover)
            title.text = mapData.title
            originPrise.text = mapData.zk_final_price
            originPrise.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            val finalPrise = (mapData.zk_final_price.toFloat() - mapData.coupon_amount)
            val finalPriseStr = String.format("%.2f", finalPrise)
            resultPrise.text = "券后价：$finalPriseStr"
        }
    }

    private var listener: OnSellItemClickListener? = null

    fun setOnSellItemClickListener(listener: OnSellItemClickListener) {
        this.listener = listener
    }

    fun updateData(result: OnSellResultItem) {
        //添加到尾部去
        val originSize = contentList.size
        val newData = result.data.tbk_dg_optimus_material_response.result_list.map_data
        this.contentList.addAll(newData)
        notifyItemRangeChanged(originSize - 1, newData.size)
    }

    interface OnSellItemClickListener {
        fun onSellItemClick(item: OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList.MapData)
    }
}