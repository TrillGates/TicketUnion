package com.sunofbeaches.ticketunion.utils

import androidx.fragment.app.Fragment
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

        private val homeFragment: HomeFragment by lazy {
            HomeFragment()
        }
        private val onSellFragment: OnSellFragment by lazy {
            OnSellFragment()
        }
        private val selectedFragment: SelectedFragment by lazy {
            SelectedFragment()
        }
        private val searchFragment: SearchFragment by lazy {
            SearchFragment()
        }

        fun getFragment(index: Int): Fragment? {
            when (index) {
                INDEX_RECOMMEND -> {
                    return homeFragment
                }
                INDEX_ON_SELL -> {
                    return onSellFragment
                }
                INDEX_SELECTED -> {
                    return selectedFragment
                }
                INDEX_SEARCH -> {
                    return searchFragment
                }
            }
            return null
        }
    }


}