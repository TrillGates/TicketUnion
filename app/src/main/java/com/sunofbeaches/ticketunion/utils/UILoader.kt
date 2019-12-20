package com.sunofbeaches.ticketunion.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.ui.views.LoadingView

abstract class UILoader(context: Context) : FrameLayout(context) {

    private var state: State = State.NONE
    private val loadingView by lazy {
        LayoutInflater.from(context).inflate(R.layout.fragment_loading, this, false)
    }

    private val emptyView by lazy {
        LayoutInflater.from(context).inflate(R.layout.fragment_empty, this, false)
    }

    private val errorView by lazy {
        LayoutInflater.from(context).inflate(R.layout.fragment_error, this, false)
    }


    enum class State {
        LOADING, SUCCESS, ERROR, EMPTY, NONE
    }

    init {
        initView()

        updateViewVisibility()
    }

    private fun initView() {
        addView(loadingView)
        addView(emptyView)
        addView(errorView)
        successView = getSuccessView(this)
        //get successView
        addView(successView)
        errorView.findViewById<View>(R.id.reload).setOnClickListener {
            onReloadClick()
        }
    }

    /**
     * on user click reload when network error.
     */
    open fun onReloadClick() {

    }

    private var successView: View? = null

    /**
     * 添加view进来
     */
    private fun updateViewVisibility() {
        loadingView.visibility = if (state == State.LOADING) View.VISIBLE else View.GONE
        if (!loadingView.isVisible) {
            loadingView.findViewById<LoadingView>(R.id.loading_view).stopRotate()
        }
        emptyView.visibility = if (state == State.EMPTY) View.VISIBLE else View.GONE
        errorView.visibility = if (state == State.ERROR) View.VISIBLE else View.GONE
        successView?.visibility = if (state == State.SUCCESS) View.VISIBLE else View.GONE
    }

    abstract fun getSuccessView(container: ViewGroup): View?

    fun updateViewByState(state: State) {
        this.state = state
        updateViewVisibility()
    }


}