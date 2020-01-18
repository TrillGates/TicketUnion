package com.sunofbeaches.ticketunion.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sunofbeaches.ticketunion.model.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.ui.fragment.HomePageFragment

class HomePageAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val categoryList: ArrayList<MainCategoryItem.Data> = ArrayList()


    override fun getItem(position: Int): Fragment {
        //创建各个Fragment
        val datum = categoryList[position]
        return HomePageFragment.newInstance(datum)
    }

    override fun getCount(): Int {
        return categoryList.size
    }

    fun setCategories(result: MainCategoryItem?) {
        if (result != null) {
            categoryList.clear()
            categoryList.addAll(result.data)
            notifyDataSetChanged()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position].title
    }
}