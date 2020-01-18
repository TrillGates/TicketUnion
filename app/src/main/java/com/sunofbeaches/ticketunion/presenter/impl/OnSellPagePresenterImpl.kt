package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.model.domain.OnSellResultItem
import com.sunofbeaches.ticketunion.presenter.OnSellPagePresenter
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.UrlUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class OnSellPagePresenterImpl : OnSellPagePresenter(){
    override fun reload() {
        loadOnSellContent()
    }

    private var currentPage = DEFAULT_PAGE


    override fun loadOnSellContent() {
        onLoading()
        currentPage = DEFAULT_PAGE
        //加载特惠数据
        val url = UrlUtils.getOnSellUrl(DEFAULT_PAGE)
        val task = mApi?.getOnSellGoods(url)
        task?.enqueue(object : Callback<OnSellResultItem> {
            override fun onFailure(call: Call<OnSellResultItem>, t: Throwable) {
                handlerLoadedFailure()
            }

            override fun onResponse(
                call: Call<OnSellResultItem>,
                response: Response<OnSellResultItem>
            ) {
                val code = response.code()
                LogUtils.d(this, "loadOnSellContent --> $code")
                if (HttpURLConnection.HTTP_OK == code) {
                    handlerContentResult(response.body())
                } else {
                    handlerLoadedFailure()
                }
            }

        })
    }

    private fun onLoading() {
        for (callback in callbacks) {
            callback.onLoading()
        }
    }

    private fun handlerContentResult(result: OnSellResultItem?) {
        if (result == null || result.data.tbk_dg_optimus_material_response.result_list.map_data.isEmpty()) {
            for (callback in callbacks) {
                callback.onEmpty()
            }
        } else {
            for (callback in callbacks) {
                callback.onSellContentLoaded(result.data.tbk_dg_optimus_material_response.result_list)
            }
        }

    }

    private fun handlerLoadedFailure() {
        for (callback in callbacks) {
            callback.onError()
        }
    }

    private var isLoadingMore = false

    override fun loaderMore() {
        if (isLoadingMore) {
            return
        }
        isLoadingMore = true
        currentPage++
        //加载更多的内容
        val url = UrlUtils.getOnSellUrl(currentPage)
        val task = mApi?.getOnSellGoods(url)
        task?.enqueue(object : Callback<OnSellResultItem> {
            override fun onFailure(call: Call<OnSellResultItem>, t: Throwable) {
                handleLoaderMoreError()
                currentPage--
                isLoadingMore = false
            }

            override fun onResponse(
                call: Call<OnSellResultItem>,
                response: Response<OnSellResultItem>
            ) {
                isLoadingMore = false
                val code = response.code()
                LogUtils.d(this, "loadOnSellContent --> $code")
                if (HttpURLConnection.HTTP_OK == code) {
                    handlerLoaderMore(response.body())
                } else {
                    handleLoaderMoreError()
                }
            }

        })
    }

    private fun handleLoaderMoreError() {
        for (callback in callbacks) {
            callback.onLoaderMoreError()
        }
    }

    private fun handlerLoaderMore(result: OnSellResultItem?) {
        if (result?.data == null || result.data.tbk_dg_optimus_material_response.result_list.map_data.isEmpty()) {
            for (callback in callbacks) {
                callback.onLoaderMoreEmpty()
            }
        } else {
            for (callback in callbacks) {
                callback.onLoaderMoreResult(result)
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }
}