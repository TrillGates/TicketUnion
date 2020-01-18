package com.sunofbeaches.ticketunion.ui.fragment


import android.content.Intent
import android.view.View
import android.widget.EditText
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.google.android.material.tabs.TabLayout
import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.model.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.presenter.impl.HomePagePresenterImpl
import com.sunofbeaches.ticketunion.ui.activity.IActivityForFragment
import com.sunofbeaches.ticketunion.ui.activity.QrScanActivity
import com.sunofbeaches.ticketunion.ui.adapter.HomePageAdapter
import com.sunofbeaches.ticketunion.utils.FragmentCreator
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.sunofbeaches.ticketunion.view.IHomePageCallback


/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class HomeFragment : BaseFragment<HomePagePresenterImpl>(), IHomePageCallback {
    override fun getOnRetryListener(): View.OnClickListener? {
        return View.OnClickListener {
            presenter?.reload()
        }
    }

    override fun getSubPresenter(): HomePagePresenterImpl? {
        return PresenterManager.getHomePagePresenterImpl()
    }

    override fun onCategoriesLoaded(result: MainCategoryItem?) {
        //分类数据回来了
        adapter?.setCategories(result)
        switchUIByState(PageState.SUCCESS)
    }

    override fun onLoading() {
        switchUIByState(PageState.LOADING)
    }

    override fun onError() {
        switchUIByState(PageState.ERROR)
    }

    override fun onEmpty() {
        switchUIByState(PageState.EMPTY)
    }

    @BindView(com.sunofbeaches.ticketunion.R.id.indicator)
    lateinit var indicator: TabLayout

    @BindView(com.sunofbeaches.ticketunion.R.id.qr_scan)
    lateinit var qrScan: View
    //
    @BindView(com.sunofbeaches.ticketunion.R.id.home_pager)
    lateinit var homePager: ViewPager

    @BindView(com.sunofbeaches.ticketunion.R.id.home_search_box)
    lateinit var homeSearchBox: EditText


    override fun getPageLayoutId(): Int {
        return com.sunofbeaches.ticketunion.R.layout.fragment_home
    }


    private var adapter: HomePageAdapter? = null

    override fun initView(rootView: View) {
        LogUtils.d(this, "initView...")
        adapter = HomePageAdapter(childFragmentManager)
        homePager.adapter = adapter
        //设置适配器
        indicator.setupWithViewPager(homePager)
    }

    companion object {
        const val REQUEST_CODE_SCAN = 1
    }

    override fun bindEvent() {
        qrScan.setOnClickListener {
            //跳转到扫码界面
            val intent = Intent(context, QrScanActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        }
        presenter?.registerCallback(this)
        homeSearchBox.setOnClickListener {
            if (activity != null && activity is IActivityForFragment) {
                (activity as IActivityForFragment).setUpFragment(FragmentCreator.INDEX_SEARCH, null)
            }
        }
    }

    override fun release() {
        presenter?.unregisterCallback(this)
    }

    override fun loadData() {
        //加载分类内容
        presenter?.loadCategories()
    }


}