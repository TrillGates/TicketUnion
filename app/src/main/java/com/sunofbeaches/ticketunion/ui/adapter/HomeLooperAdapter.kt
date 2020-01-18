package com.sunofbeaches.ticketunion.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.sunofbeaches.ticketunion.model.domain.CategoryPageItem

class HomeLooperAdapter : PagerAdapter() {

    private val contentList = ArrayList<CategoryPageItem.Data>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (contentList.size > 0) {
            Int.MAX_VALUE
        } else {
            0
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val iv = ImageView(container.context)
        val layoutForm = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        iv.layoutParams = layoutForm
        iv.scaleType = ImageView.ScaleType.CENTER_CROP
        val itemData = contentList[position % contentList.size]
        //LogUtils.d(this, "item Data -- > $itemData")
        val widthSize = container.measuredWidth / 2
        //设置数据
        val url = "https:${itemData.pict_url}_${widthSize}x${widthSize}.jpg"
        // LogUtils.d(this, "url ==> $url")
        Glide.with(container.context).load(url).into(iv)
        iv.setOnClickListener {
            itemClickListener?.onLooperItemClick(itemData)
        }
        container.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        LogUtils.d(this, "destroyItem...")
        container.removeView(`object` as View?)
    }

    fun updateData(data: List<CategoryPageItem.Data>) {
        this.contentList.clear()
        this.contentList.addAll(data)
        //LogUtils.d(this, "data size - > ${data.size}")
        notifyDataSetChanged()
    }

    fun cleanData() {
        this.contentList.clear()
    }

    private var itemClickListener: OnLooperItemClickListener? = null

    fun setOnLooperItemClickListener(listener: OnLooperItemClickListener) {
        this.itemClickListener = listener
    }


    interface OnLooperItemClickListener {
        fun onLooperItemClick(item: CategoryPageItem.Data)
    }
}