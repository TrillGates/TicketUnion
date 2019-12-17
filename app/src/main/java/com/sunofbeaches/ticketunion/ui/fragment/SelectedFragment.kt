package com.sunofbeaches.ticketunion.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseFragment

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class SelectedFragment : BaseFragment() {
    override fun subCreateView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        return inflater?.inflate(R.layout.fragment_selected, container, false)

    }
}