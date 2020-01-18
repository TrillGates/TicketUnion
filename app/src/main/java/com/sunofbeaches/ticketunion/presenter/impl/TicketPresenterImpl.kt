package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.model.domain.TickerResult
import com.sunofbeaches.ticketunion.model.domain.TicketRequestItem
import com.sunofbeaches.ticketunion.presenter.TicketPresenter
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.RetrofitManager
import com.sunofbeaches.ticketunion.view.ITicketCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class TicketPresenterImpl : TicketPresenter, BasePresenter<ITicketCallback>() {
    override fun reload() {
        loadTicketByItem(this.url!!, this.title!!, this.cover!!)
    }


    private var cover: String? = null
    private var title: String? = null
    private var url: String? = null

    private var currentState: State = State.NONE
    private var result: TickerResult? = null

    override fun loadTicketByItem(url: String, title: String, cover: String?) {
        this.url = url
        this.title = title
        this.cover = cover
        val reUrl: String
        if (url.startsWith("http") || url.startsWith("https")) {
            reUrl = url
        } else {
            reUrl = "https:$url"
        }
        //加载内容
        LogUtils.d(this, "url -- > $url")
        LogUtils.d(this, "title -- > $title")
        val requestItem = TicketRequestItem(reUrl, title)
        val ticketByUrl = RetrofitManager.instance?.getApi()?.getTicketByUrl(requestItem)
        ticketByUrl?.enqueue(object : Callback<TickerResult> {
            override fun onFailure(call: Call<TickerResult>, t: Throwable) {
                LogUtils.d(this, "getTicketByUrl  ... onFailure ... $t")
                //更新UI
                updateUiWithFailure()
            }

            override fun onResponse(call: Call<TickerResult>, response: Response<TickerResult>) {
                // response.body()?.string()?.let { LogUtils.d(TAG, it) }
                //更新UI
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    updateUiWithResponse(response.body())
                }
            }

        })
    }

    enum class State {
        LOADING, SUCCESS, ERROR, EMPTY, NONE
    }

    /**
     * 更新结果
     */
    private fun updateUiWithResponse(result: TickerResult?) {
        this.result = result
        currentState = State.SUCCESS
        for (callback in callbacks) {
            callback.onTicketLoaded(result!!, this.cover)
        }
    }

    /**
     * 让UI显示失败
     */
    private fun updateUiWithFailure() {
        currentState = State.ERROR
        for (callback in callbacks) {
            callback.onError()
        }
    }

    override fun registerCallback(callback: ITicketCallback) {
        //注册的时候，更新当前的转台
        when (currentState) {
            State.LOADING -> {
                callback.onLoading()
            }
            State.ERROR -> {
                callback.onError()
            }
            State.SUCCESS -> {
                callback.onTicketLoaded(this.result!!, this.cover)
            }
        }
        //update current state.
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }
}