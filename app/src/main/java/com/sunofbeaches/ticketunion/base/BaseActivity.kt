package com.sunofbeaches.ticketunion.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by TrillGates on 2019-12-13.
 * God bless my code!
 */
abstract class BaseActivity<T> : FragmentActivity() {

    private var unBinder: Unbinder? = null

    protected var presenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        presenter = getSubPresenter()
        unBinder = ButterKnife.bind(this)
        initView()
        initListener()
        bindData()
    }


    abstract fun getSubPresenter(): T?

    override fun onDestroy() {
        super.onDestroy()
        unBinder?.unbind()
        release()
    }

    open fun release() {

    }

    open fun initView() {

    }

    open fun bindData() {

    }

    open fun initListener() {

    }

    abstract fun getContentView(): Int


}