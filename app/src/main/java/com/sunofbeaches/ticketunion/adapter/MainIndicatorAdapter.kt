package com.sunofbeaches.ticketunion.adapter

import android.content.Context
import android.graphics.Color
import com.sunofbeaches.ticketunion.R

import com.sunofbeaches.ticketunion.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.utils.LogUtils
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class MainIndicatorAdapter : CommonNavigatorAdapter() {

    companion object {
        const val TAG = "MainIndicatorAdapter"
    }

    private val categories = ArrayList<MainCategoryItem.Data>()

    fun updateCategories(categories: MainCategoryItem) {
        this.categories.clear()
        this.categories.addAll(categories.data)
        notifyDataSetChanged()
    }

    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        //LogUtils.d(TAG, "getTitleView...$index")
        //创建view
        val colorTransitionPagerTitleView = SimplePagerTitleView(context)
        //设置一般情况下的颜色为灰色
        //设置选中情况下的颜色为黑色
        colorTransitionPagerTitleView.normalColor = Color.parseColor("#aaffffff")
        colorTransitionPagerTitleView.selectedColor = Color.parseColor("#ffffff")
        //单位sp
        colorTransitionPagerTitleView.textSize = 16f
        //设置要显示的内容
        colorTransitionPagerTitleView.text = categories[index].title
        //设置title的点击事件，这里的话，如果点击了title,那么就选中下面的viewPager到对应的index里面去
        //也就是说，当我们点击了title的时候，下面的viewPager会对应着index进行切换内容。
        colorTransitionPagerTitleView.setOnClickListener {
            //切换viewPager的内容，如果index不一样的话。
            LogUtils.d(TAG, "click --> $it")
        }
        //把这个创建好的view返回回去
        return colorTransitionPagerTitleView
    }

    override fun getCount(): Int {
       // LogUtils.d(TAG, "get count...${categories.size}")
        return categories.size
    }

    override fun getIndicator(context: Context?): IPagerIndicator {
       // LogUtils.d(TAG, "getIndicator...")
        val indicator = LinePagerIndicator(context)
        indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
        indicator.setColors(context?.getColor(R.color.white))
        return indicator
    }
}