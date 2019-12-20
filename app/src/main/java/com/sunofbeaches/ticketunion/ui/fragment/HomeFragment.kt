package com.sunofbeaches.ticketunion.ui.fragment

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.adapter.HomeContentListAdapter
import com.sunofbeaches.ticketunion.adapter.MainIndicatorAdapter
import com.sunofbeaches.ticketunion.base.BaseFragment
import com.sunofbeaches.ticketunion.domain.ContentItem
import com.sunofbeaches.ticketunion.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.presenter.IHomePresenter
import com.sunofbeaches.ticketunion.presenter.impl.HomePresenterImpl
import com.sunofbeaches.ticketunion.ui.holder.HomePageLooperHolder
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.UILoader
import com.sunofbeaches.ticketunion.view.IHomeCallback
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
class HomeFragment : BaseFragment(), IHomeCallback {

    companion object {
        const val TAG = "HomeFragment"
    }

    override fun onContentListUploaded(contentList: List<ContentItem.Data>) {
        homeContentListAdapter?.setData(contentList)
    }

    override fun onLooperLoaded(looper: List<ContentItem.Data>) {
        LogUtils.d(TAG, "onLooperLoaded -- > ${looper.size}")
        looperHolder?.setData(looper)
        uiLoader?.updateViewByState(UILoader.State.SUCCESS)
    }

    private var mainIndicatorAdapter: MainIndicatorAdapter? = null

    override fun onCategoriesLoaded(result: MainCategoryItem?) {
        //分类的数据回来了，更新一下首页分类的数据内容
        if (result != null) {
            mainIndicatorAdapter?.updateCategories(result)
        }
    }

    //整个布局的内容
    private var fragmentView: View? = null

    private val indicator by lazy { fragmentView?.findViewById<MagicIndicator>(R.id.indicator) }
    private val contentList by lazy { fragmentView?.findViewById<RecyclerView>(R.id.content_list) }


    private val mHomePresenter: IHomePresenter? = HomePresenterImpl.get()
    private var looperHolder: HomePageLooperHolder? = null
    private val looperContainer by lazy { fragmentView?.findViewById<FrameLayout>(R.id.looper_container) }

    private var homeContentListAdapter: HomeContentListAdapter? = null

    private var uiLoader: UILoader? = null


    override fun initView() {
        indicator?.setBackgroundColor(context?.getColor(R.color.origin)!!)
        val commonNavigator = CommonNavigator(context)
        //设置适配器
        mainIndicatorAdapter = MainIndicatorAdapter()
        commonNavigator.adapter = mainIndicatorAdapter
        //commonNavigator.isAdjustMode = true
        indicator?.navigator = commonNavigator
        looperHolder = context?.let { HomePageLooperHolder(it) }
        looperContainer?.addView(looperHolder?.getView())
        //列表内容
        contentList?.layoutManager = LinearLayoutManager(context)
        homeContentListAdapter = HomeContentListAdapter()
        contentList?.adapter = homeContentListAdapter
        contentList?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                outRect.bottom = UIUtil.dip2px(context, 3.0)
            }
        })
    }

    override fun loadData() {
        //努力加载中
        uiLoader?.updateViewByState(UILoader.State.LOADING)
        mHomePresenter?.registerCallback(this)
        mHomePresenter?.loadDefaultData()
    }

    override fun initEvent() {

    }

    private var recommendContainer: View? = null

    override fun subCreateView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        recommendContainer = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_recommend_success, container, false)

        if (uiLoader == null && context != null) {
            uiLoader = object : UILoader(context!!) {
                override fun getSuccessView(container: ViewGroup): View? {
                    fragmentView = LayoutInflater.from(container.context)
                        .inflate(R.layout.fragment_recommend, container, false)
                    return fragmentView
                }
            }
        }

        if (uiLoader?.parent is ViewGroup) {
            (uiLoader?.parent as ViewGroup).removeView(uiLoader)
        }

        val uiLoaderContainer =
            recommendContainer?.findViewById<FrameLayout>(R.id.recommend_container)
        uiLoaderContainer?.addView(uiLoader)
        return recommendContainer
    }
}