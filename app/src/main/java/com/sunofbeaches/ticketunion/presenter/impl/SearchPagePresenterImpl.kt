package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.model.domain.HistoryItem
import com.sunofbeaches.ticketunion.model.domain.SearchRecommend
import com.sunofbeaches.ticketunion.model.domain.SearchResult
import com.sunofbeaches.ticketunion.presenter.SearchPagerPresenter
import com.sunofbeaches.ticketunion.utils.Constants
import com.sunofbeaches.ticketunion.utils.JsonCacheUtil
import com.sunofbeaches.ticketunion.utils.LogUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection


class SearchPagePresenterImpl : SearchPagerPresenter() {
    override fun getRecommendWords() {
        //获取推荐的词
        val task = mApi?.getSearchRecommend()
        task?.enqueue(object : Callback<SearchRecommend> {
            override fun onFailure(call: Call<SearchRecommend>, t: Throwable) {
                //让UI隐藏推荐词模块
                for (callback in callbacks) {
                    callback.onRecommendWordLoadError()
                }
            }

            override fun onResponse(
                call: Call<SearchRecommend>,
                response: Response<SearchRecommend>
            ) {
                //如果数量大于0，让UI显示推荐词，否则隐藏
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(this, "result -- > ${response.body()}")
                    for (callback in callbacks) {
                        callback.onRecommenWordsLoaded(response.body()!!)
                    }
                } else {
                    for (callback in callbacks) {
                        callback.onRecommendWordLoadError()
                    }
                }
            }

        })
    }

    override fun reload() {
        //重新加载，有可能是网络问题
        doSearch(currentKeyword!!)
    }

    private var isLoadingMore = false

    override fun loaderMore() {
        if (isLoadingMore) {
            return
        }
        isLoadingMore = true
        currentPage++
        val task = mApi?.doSearch(currentPage, currentKeyword!!)
        task?.enqueue(object : Callback<SearchResult> {
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                currentPage--
                handlerLoaderMoreFailure()
                isLoadingMore = false
            }

            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                val code = response.code()
                LogUtils.d(this, "code is -- > $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    handlerLoaderMoreSuccess(response.body())
                } else {
                    handlerLoaderMoreFailure()
                }
                isLoadingMore = false
            }
        })
    }

    private fun handlerLoaderMoreSuccess(result: SearchResult?) {
        if (result == null || result?.data?.tbk_dg_material_optional_response?.result_list?.map_data?.isEmpty()) {
            for (callback in callbacks) {
                callback.onLoaderEmpty()
            }
        } else {
            for (callback in callbacks) {
                callback.onLoaderMote(result)
            }
        }
    }

    private fun handlerLoaderMoreFailure() {
        for (callback in callbacks) {
            callback.onLoaderMoreError()
        }
    }

    /**
     * 获取历史记录
     */
    override fun getHistories() {
        val cache = JsonCacheUtil.getInstance()
            .getCache(Constants.KEY_HISTORY_DATA, HistoryItem::class.java)
        for (callback in callbacks) {
            cache?.histories?.reverse()
            callback.onHistoriesLoaded(cache)
        }
    }

    private var currentPage = DEFAULT_PAGE

    companion object {

        private const val DEFAULT_PAGE = 1
        private const val HISTORY_MAX_LENGTH = 10
    }

    private var currentKeyword: String? = null

    override fun doSearch(keyword: String) {
        onLoading()
        this.currentKeyword = keyword
        LogUtils.d(this, "do search -- > $keyword")
        //去搜索咯
        val task = mApi?.doSearch(currentPage, keyword)
        task?.enqueue(object : Callback<SearchResult> {
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                handlerFailure()
            }

            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                val code = response.code()
                LogUtils.d(this, "code is -- > $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    handlerSuccess(response.body())
                } else {
                    handlerFailure()
                }
            }
        })
        //保存历史记录
        saveHistory(keyword)
    }


    private fun saveHistory(keyword: String) {
        val cache = JsonCacheUtil.getInstance()
            .getCache(Constants.KEY_HISTORY_DATA, HistoryItem::class.java)
        if (cache != null) {
            //判断是否已经存在了，如果存在的话，删除
            if (cache.histories.contains(keyword)) {
                cache.histories.remove(keyword)
            }
            cache.histories.add(keyword)
            //如果长度超出了10个，删除后面的
            if (cache.histories.size > HISTORY_MAX_LENGTH) {
                val historyItem = HistoryItem(cache.histories.subList(0, 10) as ArrayList<String>)
                JsonCacheUtil.getInstance()
                    .saveCache(Constants.KEY_HISTORY_DATA, historyItem)
            } else {
                JsonCacheUtil.getInstance()
                    .saveCache(Constants.KEY_HISTORY_DATA, cache)
            }
        } else {
            //如果没有，那么就创建咯
            val histories: ArrayList<String> = ArrayList()
            histories.add(keyword)
            val historyItem = HistoryItem(histories)
            JsonCacheUtil.getInstance()
                .saveCache(Constants.KEY_HISTORY_DATA, historyItem)
        }
    }


    private fun onLoading() {
        for (callback in callbacks) {
            callback.onLoading()
        }
    }

    private fun handlerSuccess(result: SearchResult?) {
        if (result == null ||
            result.data.tbk_dg_material_optional_response == null
            || result.data.tbk_dg_material_optional_response.result_list.map_data.isEmpty()
        ) {
            for (callback in callbacks) {
                callback.onEmpty()
            }
        } else {
            for (callback in callbacks) {
                callback.onSearchResultLoaded(result)
            }
        }

    }

    private fun handlerFailure() {
        for (callback in callbacks) {
            callback.onError()
        }
    }

}