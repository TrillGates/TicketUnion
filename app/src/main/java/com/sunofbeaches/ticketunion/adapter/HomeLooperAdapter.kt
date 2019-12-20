package com.sunofbeaches.ticketunion.adapter

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.sunofbeaches.ticketunion.domain.ContentItem
import com.sunofbeaches.ticketunion.utils.LogUtils

class HomeLooperAdapter : PagerAdapter() {

    companion object {
        const val TAG = "HomeLooperAdapter"
    }

    private val contentList = ArrayList<ContentItem.Data>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return contentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val iv = ImageView(container.context)
        val layoutForm = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        iv.layoutParams = layoutForm
        iv.scaleType = ImageView.ScaleType.CENTER_CROP
        val itemData = contentList[position]
        //设置数据
        //LogUtils.d(TAG, "itemData.pict_url ==> ${itemData.pict_url}")
        val url = "http:${itemData.pict_url}"
        //  LogUtils.d(TAG, "url ==> $url")
        Glide.with(container.context).load(url).into(iv)
        container.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun updateData(data: List<ContentItem.Data>) {
        this.contentList.clear()
        this.contentList.addAll(data)
        LogUtils.d(TAG, "data size - > ${data.size}")
        notifyDataSetChanged()
    }
}