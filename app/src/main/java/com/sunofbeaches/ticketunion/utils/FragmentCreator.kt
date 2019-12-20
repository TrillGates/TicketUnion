package com.sunofbeaches.ticketunion.utils

import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.ui.fragment.HomeFragment
import com.sunofbeaches.ticketunion.ui.fragment.OnSellFragment
import com.sunofbeaches.ticketunion.ui.fragment.SearchFragment
import com.sunofbeaches.ticketunion.ui.fragment.SelectedFragment

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class FragmentCreator {

    companion object {
        const val INDEX_RECOMMEND = 1
        const val INDEX_ON_SELL = 2
        const val INDEX_SELECTED = 3
        const val INDEX_SEARCH = 4

        private val fragmentMap = HashMap<Int, BaseFragment>()

        fun getFragment(index: Int): BaseFragment? {
            var baseFragment = fragmentMap[index]
            if (baseFragment != null) {
                return baseFragment
            } else {
                when (index) {
                    INDEX_RECOMMEND -> {
                        baseFragment = HomeFragment()
                    }
                    INDEX_ON_SELL -> {
                        baseFragment = OnSellFragment()
                    }
                    INDEX_SELECTED -> {
                        baseFragment = SelectedFragment()
                    }
                    INDEX_SEARCH -> {
                        baseFragment = SearchFragment()
                    }
                }
                //保存起来
                if (baseFragment != null) {
                    fragmentMap[index] = baseFragment
                }
                return baseFragment
            }
        }
    }


}