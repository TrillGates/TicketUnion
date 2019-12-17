package com.sunofbeaches.ticketunion.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
abstract class BaseFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return subCreateView(inflater,container)
    }

    abstract fun subCreateView(inflater: LayoutInflater?,
        container: ViewGroup?): View?
}