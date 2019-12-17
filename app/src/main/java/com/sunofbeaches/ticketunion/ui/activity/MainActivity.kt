package com.sunofbeaches.ticketunion.ui.activity

import android.widget.RadioGroup
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseActivity
import com.sunofbeaches.ticketunion.utils.FragmentCreator

class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.navigation_recommend -> {
                //推荐
                setUpFragment(FragmentCreator.INDEX_RECOMMEND)
            }
            R.id.navigation_on_sell -> {
                //特惠
                setUpFragment(FragmentCreator.INDEX_ON_SELL)
            }
            R.id.navigation_selected -> {
                //精选
                setUpFragment(FragmentCreator.INDEX_SELECTED)
            }
            R.id.navigation_search -> {
                //搜索
                setUpFragment(FragmentCreator.INDEX_SEARCH)
            }
        }
    }

    var navigationBar: RadioGroup? = null
    override fun initView() {
        navigationBar = this.findViewById<RadioGroup>(R.id.navigation_bar)
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun initListener() {
        super.initListener()
        navigationBar?.setOnCheckedChangeListener(this)
    }

    override fun bindData() {
        super.bindData()
        initFragments()
    }

    private fun initFragments() {
        setUpFragment(FragmentCreator.INDEX_RECOMMEND)
    }

    private fun setUpFragment(index: Int) {
        val fragment = FragmentCreator.getFragment(index)
        if (fragment != null) {
            val supportFragmentManager = supportFragmentManager
            val beginTransaction = supportFragmentManager.beginTransaction()
            beginTransaction.replace(R.id.page_container, fragment, null)
            beginTransaction.commit()
        }

    }

}
