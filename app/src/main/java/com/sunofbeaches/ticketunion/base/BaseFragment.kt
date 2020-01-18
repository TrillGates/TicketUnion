package com.sunofbeaches.ticketunion.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.sunofbeaches.ticketunion.R


/**
 * Created by TrillGates on 2019-12-15.
 * God bless my code!
 */
abstract class BaseFragment<P> : Fragment() {

    enum class PageState {
        NONE, LOADING, ERROR, EMPTY, SUCCESS
    }

    private var bind: Unbinder? = null

    protected var presenter: P? = null

    private var rootView: ViewGroup? = null

    abstract fun getSubPresenter(): P?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //载入有状态的View
        rootView = getRootView(inflater, container)
        val containerView = rootView?.findViewById<ViewGroup>(R.id.fragment_container)
        setUpView(rootView, containerView, inflater)
        presenter = getSubPresenter()
        //设置相关的事件
        bindEvent()
        loadData()
        return rootView
    }

    open fun getRootView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewGroup? {
        return inflater.inflate(R.layout.fragment_container, container, false) as ViewGroup
    }

    private var successView: View? = null
    private var loadingView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null

    private fun setUpView(rootView: ViewGroup?, container: ViewGroup?, inflater: LayoutInflater) {
        //成功的View
        successView = subCreateView(inflater, container)
        container?.addView(successView)
        //加载中的View
        loadingView = createLoadingView(inflater, container)
        container?.addView(loadingView)
        //网络错误的View
        errorView = createErrorView(inflater, container)
        container?.addView(errorView)
        errorView?.setOnClickListener(getOnRetryListener())
        //数据为空的界面
        emptyView = createEmptyView(inflater, container)
        container?.addView(emptyView)
        switchUIByState(PageState.NONE)
        bind = ButterKnife.bind(this, rootView!!)
        initView(successView!!)
    }

    abstract fun getOnRetryListener(): View.OnClickListener?


    //根据状态显示UI
    fun switchUIByState(state: PageState) {
        successView?.visibility = if (state == PageState.SUCCESS) View.VISIBLE else View.GONE
        loadingView?.visibility = if (state == PageState.LOADING) View.VISIBLE else View.GONE
        errorView?.visibility = if (state == PageState.ERROR) View.VISIBLE else View.GONE
        emptyView?.visibility = if (state == PageState.EMPTY) View.VISIBLE else View.GONE
        if (state == PageState.NONE) {
            successView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
            errorView?.visibility = View.GONE
            emptyView?.visibility = View.GONE
        }
    }

    open fun createEmptyView(inflater: LayoutInflater, rootView: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_empty, rootView, false)
    }

    open fun createErrorView(inflater: LayoutInflater, rootView: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_error, rootView, false)
    }

    /**
     * 如果子类需要改变，覆盖即可
     */
    open fun createLoadingView(inflater: LayoutInflater, rootView: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_loading, rootView, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //取消绑定
        bind?.unbind()
        release()
    }

    open fun release() {

    }

    private fun subCreateView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(getPageLayoutId(), container, false)
    }

    abstract fun getPageLayoutId(): Int


    open fun initView(rootView: View) {

    }

    open fun loadData() {

    }

    open fun bindEvent() {

    }
}