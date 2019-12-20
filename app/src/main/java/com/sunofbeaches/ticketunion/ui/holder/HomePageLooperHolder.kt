package com.sunofbeaches.ticketunion.ui.holder

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.adapter.HomeLooperAdapter
import com.sunofbeaches.ticketunion.base.IBaseHolder
import com.sunofbeaches.ticketunion.domain.ContentItem
import com.sunofbeaches.ticketunion.utils.LogUtils

class HomePageLooperHolder(context: Context) : FrameLayout(context),
    IBaseHolder<List<ContentItem.Data>> {

    private var homeLooperAdapter: HomeLooperAdapter? = null

    override fun setData(data: List<ContentItem.Data>) {
        homeLooperAdapter?.updateData(data)
    }

    override fun getView(): View {
        return this
    }

    private val pager: ViewPager by lazy { this.findViewById<ViewPager>(R.id.looper_pager) }

    companion object{
        const val TAG = "HomePageLooperHolder"
    }

    init {
        LogUtils.d(TAG, "holder init...")
        //把UI载入
        View.inflate(context, R.layout.home_loper_layout, this)
        //设置适配器
        homeLooperAdapter = HomeLooperAdapter()
        pager.adapter = homeLooperAdapter
    }


}