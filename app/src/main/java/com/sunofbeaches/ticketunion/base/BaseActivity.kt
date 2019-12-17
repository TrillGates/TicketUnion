package com.sunofbeaches.ticketunion.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Created by TrillGates on 2019-12-13.
 * God bless my code!
 */
abstract class BaseActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        initView()
        initListener()
        bindData()
    }

    abstract fun initView()

    open fun bindData(){

    }

    open fun initListener(){

    }

    abstract fun getContentView(): Int


}