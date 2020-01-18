package com.sunofbeaches.ticketunion.ui.activity

import android.os.Bundle
import android.widget.RadioGroup
import butterknife.BindView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseActivity
import com.sunofbeaches.ticketunion.utils.FragmentCreator
import com.sunofbeaches.ticketunion.utils.UIUtils

class MainActivity : BaseActivity<Any>(), RadioGroup.OnCheckedChangeListener, IActivityForFragment {
    override fun getSubPresenter(): Any? {
        return null
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.navigation_recommend -> {
                //推荐
                setUpFragment(FragmentCreator.INDEX_RECOMMEND, null)
            }
            R.id.navigation_on_sell -> {
                //特惠
                setUpFragment(FragmentCreator.INDEX_ON_SELL, null)
            }
            R.id.navigation_selected -> {
                //精选
                setUpFragment(FragmentCreator.INDEX_SELECTED, null)
            }
            R.id.navigation_search -> {
                //搜索
                setUpFragment(FragmentCreator.INDEX_SEARCH, null)
            }
        }
    }

    private fun updateNavigationCheckedByPosition(index: Int) {
        when (index) {
            FragmentCreator.INDEX_RECOMMEND -> {
                navigationBar.check(R.id.navigation_recommend)
            }
            FragmentCreator.INDEX_ON_SELL -> {
                navigationBar.check(R.id.navigation_on_sell)
            }
            FragmentCreator.INDEX_SELECTED -> {
                navigationBar.check(R.id.navigation_selected)
            }
            FragmentCreator.INDEX_SEARCH -> {
                navigationBar.check(R.id.navigation_search)
            }
        }

    }

    @BindView(R.id.navigation_bar)
    lateinit var navigationBar: RadioGroup

    override fun initView() {
        val supportFragmentManager = supportFragmentManager
        val transaction = supportFragmentManager.beginTransaction()
        val homeFragment = FragmentCreator.getFragment(FragmentCreator.INDEX_RECOMMEND)
        transaction.add(
            R.id.page_container,
            homeFragment!!,
            FragmentCreator.INDEX_RECOMMEND.toString()
        )
        lastFragmentIndex = FragmentCreator.INDEX_RECOMMEND
        //
        val onSellFragment = FragmentCreator.getFragment(FragmentCreator.INDEX_ON_SELL)
        transaction.add(
            R.id.page_container,
            onSellFragment!!,
            FragmentCreator.INDEX_ON_SELL.toString()
        )
        transaction.hide(onSellFragment)
        //
        val selectFragment = FragmentCreator.getFragment(FragmentCreator.INDEX_SELECTED)
        transaction.add(
            R.id.page_container,
            selectFragment!!,
            FragmentCreator.INDEX_SELECTED.toString()
        )
        transaction.hide(selectFragment)
        //
        val searchFragment = FragmentCreator.getFragment(FragmentCreator.INDEX_SEARCH)
        transaction.add(
            R.id.page_container,
            searchFragment!!,
            FragmentCreator.INDEX_SEARCH.toString()
        )
        transaction.hide(searchFragment)
        transaction.commit()
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun initListener() {
        super.initListener()
        navigationBar.setOnCheckedChangeListener(this)
    }


    private var lastFragmentIndex = -1

    override fun setUpFragment(index: Int, bundle: Bundle?) {
        if (index == lastFragmentIndex) {
            return
        }
        val fragment = FragmentCreator.getFragment(index)
        fragment?.arguments = bundle
        if (fragment != null) {
            val supportFragmentManager = supportFragmentManager
            val transaction = supportFragmentManager.beginTransaction()
            if (lastFragmentIndex != -1) {
                val lastFragment = FragmentCreator.getFragment(lastFragmentIndex)
                transaction.hide(lastFragment!!)
            }
            if (!fragment.isAdded) {
                transaction.add(R.id.page_container, fragment, index.toString())
            } else {
                transaction.show(fragment)
            }
            lastFragmentIndex = index
            transaction.commit()
        }
        //更新一个选项
        updateNavigationCheckedByPosition(index)
    }

    //1.5秒内按两次退出
    private val backPressDuration = 2000

    private var lastPressTime: Long = 0L

    override fun onBackPressed() {
        //处理2次再退出的问题
        val currentMillis = System.currentTimeMillis()
        if (currentMillis - lastPressTime > backPressDuration) {
            UIUtils.toast("再来一次退出程序.")
            lastPressTime = currentMillis
        } else {
            super.onBackPressed()
        }
    }

}
