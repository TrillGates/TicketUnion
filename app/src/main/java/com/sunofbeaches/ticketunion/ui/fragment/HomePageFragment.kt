package com.sunofbeaches.ticketunion.ui.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.model.domain.CategoryPageItem
import com.sunofbeaches.ticketunion.model.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.presenter.impl.CategoryPagePresenterImpl
import com.sunofbeaches.ticketunion.ui.activity.TicketActivity
import com.sunofbeaches.ticketunion.ui.adapter.HomeContentListAdapter
import com.sunofbeaches.ticketunion.ui.adapter.HomeLooperAdapter
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.sunofbeaches.ticketunion.utils.UIUtils
import com.sunofbeaches.ticketunion.view.IHomeCategoryPageCallback

class HomePageFragment : BaseFragment<CategoryPagePresenterImpl>(),
    IHomeCategoryPageCallback {
    override fun getOnRetryListener(): View.OnClickListener? {
        return View.OnClickListener {
            presenter?.reload()
        }
    }

    override fun onLoaderMoreEmpty(categoryId: Int) {
        //设置不能再刷新，并且提示用户已经加载完了
        refreshLayout.finishLoadmore()
        refreshLayout.setEnableLoadmore(false)
    }

    override fun onLoaderMoreError(categoryId: Int) {
        //提示用户网络错误，请稍后重试
        refreshLayout.finishLoadmore()
    }

    override fun onLoaderMoreLoaded(result: CategoryPageItem, categoryId: Int) {
        //把数据添加到适配器列表的后面
        if (arguments?.getInt(CATEGORY_ID_KEY) == categoryId) {
            refreshLayout.finishLoadmore()
            adapter?.updateData(result.data)
            //结束UI刷新动作
            UIUtils.toast("拉取了${result.data.size}条数据")
        }
    }

    override fun onLooperDataLoaded(looper: List<CategoryPageItem.Data>, categoryId: Int) {
        if (arguments?.getInt(CATEGORY_ID_KEY) == categoryId) {
            looperAdapter?.updateData(looper)
        }
    }

    override fun onContentLoaded(result: CategoryPageItem, categoryId: Int) {
        if (arguments?.getInt(CATEGORY_ID_KEY) == categoryId) {
            //内容加载加载回来了
            adapter?.setData(result.data)
            switchUIByState(PageState.SUCCESS)
        }
    }

    override fun getSubPresenter(): CategoryPagePresenterImpl? {
        return PresenterManager.getCategoryPagePresenterImpl()
    }

    override fun onLoading(key: Any) {
        if (arguments?.getInt(CATEGORY_ID_KEY) == key) {
            switchUIByState(PageState.LOADING)
        }
    }

    override fun onError(key: Any) {
        if (arguments?.getInt(CATEGORY_ID_KEY) == key) {
            switchUIByState(PageState.ERROR)
        }
    }

    override fun onEmpty(key: Any) {
        if (arguments?.getInt(CATEGORY_ID_KEY) == key) {
            switchUIByState(PageState.EMPTY)
        }
    }

    companion object {
        private const val CATEGORY_TITLE_KEY = "category_title"
        private const val CATEGORY_ID_KEY = "category_id"
        fun newInstance(datum: MainCategoryItem.Data): HomePageFragment {
            val bundle = Bundle()
            bundle.putString(CATEGORY_TITLE_KEY, datum.title)
            bundle.putInt(CATEGORY_ID_KEY, datum.id)
            val fragment = HomePageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getPageLayoutId(): Int {
        return R.layout.fragment_home_pager
    }

    @BindView(R.id.category_tips_text)
    lateinit var tipsText: TextView

    @BindView(R.id.content_list)
    lateinit var contentList: RecyclerView


    @BindView(R.id.main_looper_pager)
    lateinit var looperPager: ViewPager

    @BindView(R.id.home_content_refresh)
    lateinit var refreshLayout: TwinklingRefreshLayout


    private var adapter: HomeContentListAdapter? = null
    private var looperAdapter: HomeLooperAdapter? = null


    override fun initView(rootView: View) {
        adapter = HomeContentListAdapter()
        contentList.adapter = adapter
        contentList.layoutManager = LinearLayoutManager(context)
        looperAdapter = HomeLooperAdapter()
        looperPager.adapter = looperAdapter
        tipsText.text = arguments?.getString(CATEGORY_TITLE_KEY)
        contentList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = 10
                outRect.bottom = 10
            }
        })
        //设置刷新相关的属性
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableOverScroll(true)
        refreshLayout.setEnableLoadmore(true)
        //设置样式
        val refreshFooterView = LoadingView(context)
        refreshLayout.setBottomView(refreshFooterView)
    }

    override fun bindEvent() {
        looperAdapter?.setOnLooperItemClickListener(object :
            HomeLooperAdapter.OnLooperItemClickListener {
            override fun onLooperItemClick(item: CategoryPageItem.Data) {
                toTicketPage(item)
            }

        })
        adapter?.setItemClickListener(object : HomeContentListAdapter.OnItemClickListener {
            override fun onItemClick(item: CategoryPageItem.Data, index: Int) {
                toTicketPage(item)
            }
        })
        presenter?.registerCallback(this)
        //设置刷新相关的回调
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                LogUtils.d(HomePageFragment, "on loadMore....")
                //加载更多内容，数据回来添加到头部
                presenter?.loaderMore(arguments?.getInt(CATEGORY_ID_KEY)!!)
            }
        })
    }

    private fun toTicketPage(item: CategoryPageItem.Data) {
        val ticketPresenterImpl = PresenterManager.getTicketPresenterImpl()
        //点击了以后呢，去获取淘口令
        if (TextUtils.isEmpty(item.coupon_click_url)) {
            ticketPresenterImpl.loadTicketByItem(item.click_url, item.title, item.pict_url)
        } else {
            ticketPresenterImpl.loadTicketByItem(
                item.coupon_click_url,
                item.title,
                item.pict_url
            )
        }
        //跳转到对领券界面
        startActivity(Intent(context, TicketActivity::class.java))
    }

    override fun release() {
        LogUtils.d(this, "release...")
        presenter?.unregisterCallback(this)
        adapter?.cleanData()
        adapter = null
        looperAdapter?.cleanData()
        looperAdapter = null
    }

    override fun loadData() {
        val categoryId = arguments?.getInt(CATEGORY_ID_KEY)
        presenter?.getCategoryContentById(categoryId!!)
    }

}