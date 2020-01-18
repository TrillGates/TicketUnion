package com.sunofbeaches.ticketunion.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.model.domain.PickContentItem
import com.sunofbeaches.ticketunion.model.domain.PickTypeItem
import com.sunofbeaches.ticketunion.presenter.impl.SelectedPagePresenterImpl
import com.sunofbeaches.ticketunion.ui.activity.TicketActivity
import com.sunofbeaches.ticketunion.ui.adapter.PickContentListAdapter
import com.sunofbeaches.ticketunion.ui.adapter.PickTypeAdapter
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.sunofbeaches.ticketunion.view.ISelectedPageCallback

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class SelectedFragment : BaseFragment<SelectedPagePresenterImpl>(), ISelectedPageCallback {
    override fun getOnRetryListener(): View.OnClickListener? {
        return View.OnClickListener {
            presenter?.reload()
        }
    }

    override fun onPickTypeLoaded(pickTypeItem: PickTypeItem) {
        typeAdapter?.setData(pickTypeItem)
        //移动到顶部
        pickContentList.scrollToPosition(0)
        //根据Id加载内容了
        presenter?.loadPickContentById(pickTypeItem.data[0].favorites_id)
    }

    override fun onContentListLoaded(pickContentItem: PickContentItem) {
        contentListAdapter?.setData(pickContentItem)
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

    override fun getSubPresenter(): SelectedPagePresenterImpl? {
        return PresenterManager.getSelectedPagePresenterImpl()
    }

    private var typeAdapter: PickTypeAdapter? = null


    @BindView(R.id.left_type_list)
    lateinit var typeList: RecyclerView


    @BindView(R.id.right_content_list)
    lateinit var pickContentList: RecyclerView

    @BindView(R.id.page_header)
    lateinit var pageHeader: View


    //列表的适配器
    private var contentListAdapter: PickContentListAdapter? = null


    override fun initView(rootView: View) {
        //左侧分类设置适配器
        if (typeAdapter == null) {
            typeAdapter = PickTypeAdapter()
        }
        typeList.layoutManager = LinearLayoutManager(context)
        typeList.adapter = typeAdapter
        if (contentListAdapter == null) {
            contentListAdapter = PickContentListAdapter()
        }

        //给列表设置适配器
        pickContentList.adapter = contentListAdapter
        pickContentList.layoutManager = LinearLayoutManager(context)
    }

    override fun loadData() {
        presenter?.loadPickType()
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
                pickContentList.smoothScrollToPosition(0)
            }
        }
        presenter?.registerCallback(this)
        typeAdapter?.setOnItemClickListener(object : PickTypeAdapter.OnItemClickListener {
            override fun onTypeItemClick(item: PickTypeItem.Data) {
                //加载对应页面的数据
                presenter?.loadPickContentById(item.favorites_id)
                typeAdapter?.updateSelectedId(item.favorites_id)
            }
        })
        contentListAdapter?.setOnPickerListItemClickListener(object :
            PickContentListAdapter.OnPickerListItemClickListener {
            override fun onPickItemClick(item: PickContentItem.Data.TbkUatmFavoritesItemGetResponse.Results.UatmTbkItem) {
                //判断有没有优惠链接，如果没有，则使用推荐链接
                val ticKetPresenter = PresenterManager.getTicketPresenterImpl()
                if (TextUtils.isEmpty(item.coupon_click_url)) {
                    ticKetPresenter.loadTicketByItem(item.click_url, item.title, item.pict_url)
                } else {
                    ticKetPresenter.loadTicketByItem(
                        item.coupon_click_url,
                        item.title,
                        item.pict_url
                    )
                }
                startActivity(Intent(context, TicketActivity::class.java))
            }
        })
    }

    override fun release() {
        presenter?.unregisterCallback(this)
    }

    override fun getPageLayoutId(): Int {
        return R.layout.fragment_selected
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): ViewGroup? {
        return inflater.inflate(R.layout.fragment_with_header_title, container, false) as
                ViewGroup
    }
}