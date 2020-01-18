package com.sunofbeaches.ticketunion.ui.fragment

import android.content.Intent
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.model.domain.HistoryItem
import com.sunofbeaches.ticketunion.model.domain.SearchRecommend
import com.sunofbeaches.ticketunion.model.domain.SearchResult
import com.sunofbeaches.ticketunion.presenter.impl.SearchPagePresenterImpl
import com.sunofbeaches.ticketunion.ui.activity.TicketActivity
import com.sunofbeaches.ticketunion.ui.adapter.SearchResultAdapter
import com.sunofbeaches.ticketunion.ui.views.FlowTextLayout
import com.sunofbeaches.ticketunion.utils.*
import com.sunofbeaches.ticketunion.view.ISearchPageCallback

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class SearchFragment : BaseFragment<SearchPagePresenterImpl>(), ISearchPageCallback {
    override fun onRecommendWordLoadError() {
        recommendContainer.visibility = View.GONE
        recommendView.contentList.clear()
    }

    override fun onRecommenWordsLoaded(recommendWord: SearchRecommend) {
        if (recommendWord.data.isEmpty()) {
            recommendContainer.visibility = View.GONE
            recommendView.contentList.clear()
        } else {
            recommendContainer.visibility = View.VISIBLE
            val result: ArrayList<String> = ArrayList()
            for (datum in recommendWord.data) {
                result.add(datum.keyword)
            }
            recommendView.setTextContents(result)
        }
    }

    override fun getOnRetryListener(): View.OnClickListener? {
        //网络不好时，点击重试
        return View.OnClickListener {
            presenter?.reload()
        }
    }

    override fun onLoaderMoreError() {
        UIUtils.toast("网络错误，请稍后重试.")
        refreshLayout.finishLoadmore()
    }

    override fun onLoaderEmpty() {
        UIUtils.toast("没有更多内容.")
        refreshLayout.finishLoadmore()
        refreshLayout.setEnableLoadmore(false)
    }

    override fun onHistoriesLoaded(histories: HistoryItem?) {
        //加载搜索记录
        switchUIByState(PageState.SUCCESS)
        //
        if (histories == null || histories.histories.size == 0) {
            historyViewContainer.visibility = View.GONE
            historyView.contentList.clear()
        } else {
            historyView.setTextContents(histories.histories)
        }
    }

    override fun onSearchResultLoaded(result: SearchResult) {
        //设置数据呀
        switchUIByState(PageState.SUCCESS)
        resultAdapter?.setData(result)
        //搜索历史不可见
        switch2ResultPage()
        KeyBoardUtils.hideKeyboard(context!!, inputBox)
    }

    override fun onLoaderMote(result: SearchResult) {
        resultAdapter?.updateData(result)
        //处理数据
        refreshLayout.finishLoadmore()
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

    override fun getSubPresenter(): SearchPagePresenterImpl? {
        return PresenterManager.getSearchPagePresenterImpl()
    }

    override fun getPageLayoutId(): Int {
        return R.layout.fragment_search_page
    }

    @BindView(R.id.search_recommend)
    lateinit var recommendView: FlowTextLayout

    @BindView(R.id.search_history)
    lateinit var historyView: FlowTextLayout

    @BindView(R.id.recommend_container)
    lateinit var recommendContainer: View

    @BindView(R.id.history_container)
    lateinit var historyViewContainer: View

    @BindView(R.id.search_input_box)
    lateinit var inputBox: EditText

    @BindView(R.id.search_refresh)
    lateinit var refreshLayout: TwinklingRefreshLayout

    @BindView(R.id.search_text)
    lateinit var searchBtnText: TextView

    @BindView(R.id.search_input_del)
    lateinit var searchInputDel: View

    @BindView(R.id.history_del_icon)
    lateinit var historyDelBtn: View

    @BindView(R.id.search_result)
    lateinit var searchResult: RecyclerView

    private var resultAdapter: SearchResultAdapter? = null

    override fun initView(rootView: View) {
        //设置刷新相关的属性
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableOverScroll(true)
        refreshLayout.setEnableLoadmore(true)
        //设置样式
        val refreshFooterView = LoadingView(context)
        refreshLayout.setBottomView(refreshFooterView)
        //创建适配器
        if (resultAdapter == null) {
            resultAdapter = SearchResultAdapter()
        }
        searchResult.layoutManager = LinearLayoutManager(context)
        searchResult.adapter = resultAdapter
        searchResult.addItemDecoration(object : RecyclerView.ItemDecoration() {
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
        inputBox.showSoftInputOnFocus = true
    }

    /**
     * 覆写掉获取根布局的方法,提供一个有搜索头部的容器
     */
    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): ViewGroup? {
        val rootView = inflater.inflate(R.layout.fragment_search, container!!, false) as ViewGroup
        rootView.setOnTouchListener { v, event ->
            KeyBoardUtils.hideKeyboard(context!!, v!!)
            false
        }
        return rootView
    }

    override fun bindEvent() {
        resultAdapter?.setOnSearchItemClickListener(object :
            SearchResultAdapter.OnSearchItemClickListener {
            override fun onSearchItemClick(item: SearchResult.Data.TbkDgMaterialOptionalResponse.ResultList.MapData) {
                val ticketPresenterImpl = PresenterManager.getTicketPresenterImpl()
                if (TextUtils.isEmpty(item.coupon_share_url)) {
                    ticketPresenterImpl.loadTicketByItem(item.url, item.title, item.pict_url)

                } else {
                    ticketPresenterImpl.loadTicketByItem(
                        item.coupon_share_url,
                        item.title,
                        item.pict_url
                    )
                }
                startActivity(Intent(context, TicketActivity::class.java))
            }

        })
        searchInputDel.setOnClickListener {
            //清除输入框里的东西
            inputBox.requestFocus()
            searchResult.scrollToPosition(0)
            inputBox.text.clear()
        }
        searchBtnText.setOnClickListener {
            //判断是否有内容
            val keyword = inputBox.text.toString().trim()
            if (keyword.isNotEmpty()) {
                presenter?.doSearch(keyword)
            }
        }
        //监听内容变化
        inputBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = s.toString()
                if (content.isEmpty()) {
                    searchInputDel.visibility = View.GONE
                    //展示历史记录
                    switchUIByState(PageState.SUCCESS)
                    switch2HistoryPage()
                } else {
                    searchInputDel.visibility = View.VISIBLE
                }
                //如果size不为0，那么显示删除的东西
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        presenter?.registerCallback(this)
        historyDelBtn.setOnClickListener {
            JsonCacheUtil.getInstance().delCache(Constants.KEY_HISTORY_DATA)
            switch2HistoryPage()
        }
        historyView.setClickListener(object : FlowTextLayout.ItemClickListener {
            override fun onItemClick(text: String) {
                inputBox.setText(text)
                inputBox.setSelection(text.length)
                searchBtnText.performClick()
            }
        })
        recommendView.setClickListener(object : FlowTextLayout.ItemClickListener {
            override fun onItemClick(text: String) {
                inputBox.setText(text)
                inputBox.setSelection(text.length)
                searchBtnText.performClick()
            }
        })
        inputBox.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBtnText.performClick()
            }
            false
        }
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                //加载更多的搜索内容
                presenter?.loaderMore()
            }
        })
    }

    override fun release() {
        presenter?.unregisterCallback(this)
    }


    override fun loadData() {
        switch2HistoryPage()
    }

    private fun switch2ResultPage() {
        historyViewContainer.visibility = View.GONE
        recommendContainer.visibility = View.GONE
        refreshLayout.visibility = View.VISIBLE
    }

    fun switch2HistoryPage() {
        presenter?.getHistories()
        presenter?.getRecommendWords()
        if (historyView.contentList.size != 0) {
            historyViewContainer.visibility = View.VISIBLE
        }

        if (recommendView.contentList.size != 0) {
            recommendContainer.visibility = View.VISIBLE
        }
        refreshLayout.visibility = View.GONE
    }


}