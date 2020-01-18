package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.model.domain.PickContentItem
import com.sunofbeaches.ticketunion.model.domain.PickTypeItem
import com.sunofbeaches.ticketunion.presenter.SelectedPagePresenter
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.UrlUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class SelectedPagePresenterImpl : SelectedPagePresenter() {
    override fun reload() {
        loadPickType()
    }

    override fun loadPickType() {
        onLoading()
        val task = mApi?.loadPickTypeList()
        task?.enqueue(object : Callback<PickTypeItem> {
            override fun onFailure(call: Call<PickTypeItem>, t: Throwable) {
                handlerFailure()
            }

            override fun onResponse(call: Call<PickTypeItem>, response: Response<PickTypeItem>) {
                val code = response.code()
                LogUtils.d(this, "code is -- > $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    handlerPickerTypeResult(response.body())
                }
            }

        })
    }

    private fun onLoading() {
        for (callback in callbacks) {
            callback.onLoading()
        }
    }

    private fun handlerPickerTypeResult(result: PickTypeItem?) {
        if (result == null || result.data.isEmpty()) {
            for (callback in callbacks) {
                callback.onEmpty()
            }
        } else {
            for (callback in callbacks) {
                callback.onPickTypeLoaded(result)
            }
        }
    }

    private fun handlerFailure() {
        for (callback in callbacks) {
            callback.onError()
        }
    }

    override fun loadPickContentById(categoryId: Int) {
        val url = UrlUtils.getTypContentUrl(categoryId)
        val task = mApi?.getPickContentList(url)
        task?.enqueue(object : Callback<PickContentItem> {
            override fun onFailure(call: Call<PickContentItem>, t: Throwable) {
                handlerFailure()
            }

            override fun onResponse(
                call: Call<PickContentItem>,
                response: Response<PickContentItem>
            ) {
                val code = response.code()
                LogUtils.d(this, "code is -- > $code")
                if (HttpURLConnection.HTTP_OK == code) {
                    handleContentList(response.body())
                } else {
                    handlerFailure()
                }
            }

        })
    }

    private fun handleContentList(result: PickContentItem?) {
        if (result == null || result.data?.tbk_uatm_favorites_item_get_response?.results?.uatm_tbk_item?.isEmpty()) {
            for (callback in callbacks) {
                callback.onEmpty()
            }
        } else {
            for (callback in callbacks) {
                callback.onContentListLoaded(result)
            }
        }

    }
}