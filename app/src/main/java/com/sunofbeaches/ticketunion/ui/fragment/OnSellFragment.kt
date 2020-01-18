package com.sunofbeaches.ticketunion.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.model.domain.OnSellResultItem
import com.sunofbeaches.ticketunion.presenter.impl.OnSellPagePresenterImpl
import com.sunofbeaches.ticketunion.ui.activity.TicketActivity
import com.sunofbeaches.ticketunion.ui.adapter.OnSellListAdapter
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.sunofbeaches.ticketunion.utils.UIUtils
import com.sunofbeaches.ticketunion.view.IOnSellPageCallback

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class OnSellFragment : BaseFragment<OnSellPagePresenterImpl>(), IOnSellPageCallback {
    override fun getOnRetryListener(): View.OnClickListener? {
        return View.OnClickListener {
            presenter?.reload()
        }
    }

    override fun onLoaderMoreResult(result: OnSellResultItem) {
        listAdapter?.updateData(result)
        //添加到适配器的结尾
        refreshLayout.finishLoadmore()
    }

    override fun onLoaderMoreError() {
        //提示用户，请稍后充实
        UIUtils.toast("网络错误，请稍后重试.")
        refreshLayout.finishLoadmore()
    }

    override fun onLoaderMoreEmpty() {
        refreshLayout.finishLoadmore()
        UIUtils.toast("没有更多内容了哦~")
        refreshLayout.setEnableLoadmore(false)
    }

    override fun onSellContentLoaded(result: OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList) {
        listAdapter?.setData(result)
        switchUIByState(PageState.SUCCESS)
    }

    @BindView(R.id.on_sell_list_view)
    lateinit var onSellListView: RecyclerView

    @BindView(R.id.page_header)
    lateinit var pageHeader: View


    @BindView(R.id.on_sell_refresh)
    lateinit var refreshLayout: TwinklingRefreshLayout

    private var listAdapter: OnSellListAdapter? = null

    override fun initView(rootView: View) {
        if (listAdapter == null) {
            listAdapter = OnSellListAdapter()
        }
        onSellListView.adapter = listAdapter
        //分为2列
        onSellListView.layoutManager = GridLayoutManager(context, 2)
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableOverScroll(true)
        refreshLayout.setEnableLoadmore(true)
        //设置样式
        val refreshFooterView = LoadingView(context)
        refreshLayout.setBottomView(refreshFooterView)
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

    override fun getSubPresenter(): OnSellPagePresenterImpl? {
        return PresenterManager.getOnSellPagePresenterImpl()
    }

    override fun loadData() {
        //加载数据
        presenter?.loadOnSellContent()
    }

    //1秒钟内点击两次才算double click
    private val doubleClickDuration = 1000
    private var lastClickTime = 0L

    override fun bindEvent() {
        pageHeader.setOnClickListener {
            //做一个doubleClick回到顶部的效果
            val currentMills = System.currentTimeMillis()
            if (currentMills - lastClickTime > doubleClickDuration) {
                lastClickTime = currentMills
            } else {
                //列表回到顶部
                onSellListView.smoothScrollToPosition(0)
            }
        }
        presenter?.registerCallback(this)
        listAdapter?.setOnSellItemClickListener(object : OnSellListAdapter.OnSellItemClickListener {
            override fun onSellItemClick(item: OnSellResultItem.Data.TbkDgOptimusMaterialResponse.ResultList.MapData) {
                val presenter = PresenterManager.getTicketPresenterImpl()
                if (TextUtils.isEmpty(item.coupon_click_url)) {
                    presenter.loadTicketByItem(item.click_url, item.title, item.pict_url)
                } else {
                    presenter.loadTicketByItem(item.coupon_click_url, item.title, item.pict_url)
                }
                startActivity(Intent(context, TicketActivity::class.java))
            }
        })

        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                //加载更多
                presenter?.loaderMore()
            }
        })
    }

    override fun getPageLayoutId(): Int {
        return R.layout.fragment_on_sell_page
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): ViewGroup? {
        return inflater.inflate(R.layout.fragment_with_header_title, container, false) as
                ViewGroup
    }

}