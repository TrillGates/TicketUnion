package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.domain.ContentItem
import com.sunofbeaches.ticketunion.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.presenter.IHomePresenter
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.RetrofitManager
import com.sunofbeaches.ticketunion.view.IHomeCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by TrillGates on 2019-12-17.
 * God bless my code!
 */
class HomePresenterImpl private constructor() : IHomePresenter {

    private val callbacks = ArrayList<IHomeCallback>()

    companion object {
        const val TAG = "HomePresenterImpl"

        private var sInstance: IHomePresenter? = null
            get() {
                if (field == null) {
                    field = HomePresenterImpl()
                }
                return field
            }

        fun get(): IHomePresenter {
            return sInstance!!
        }
    }


    override fun loadDefaultData() {
        LogUtils.d(TAG, "loadDefaultData...")
        //加载数据
        val categories = RetrofitManager.instance?.getApi()?.getCategories()
        categories?.enqueue(object : Callback<MainCategoryItem> {
            override fun onFailure(call: Call<MainCategoryItem>, t: Throwable) {
                LogUtils.d(TAG, "categories -> onFailure...${t}")
            }

            override fun onResponse(
                call: Call<MainCategoryItem>,
                response: Response<MainCategoryItem>
            ) {
                LogUtils.d(TAG, "get categories success.")
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    //更新UI
                    updateCategories(response.body())
                }
            }

        })
    }

    private fun updateCategories(result: MainCategoryItem?) {
        //去加载第一个分类回来
        if (result?.data?.size != 0) {
            loadLooperContent(result?.data?.get(0)?.id!!)
        }
        for (callback in callbacks) {
            callback.onCategoriesLoaded(result)
        }
    }

    fun loadLooperContent(materialId: Int) {
        val url = "discovery/$materialId/1"
        LogUtils.d(TAG, "url -- > $url")
        val task = RetrofitManager.instance?.getApi()?.getContentListByMaterialId(url)
        task?.enqueue(object : Callback<ContentItem> {
            override fun onFailure(call: Call<ContentItem>, t: Throwable) {
                LogUtils.d(TAG, "onFailure -- > $t")
            }

            override fun onResponse(call: Call<ContentItem>, response: Response<ContentItem>) {
                LogUtils.d(TAG, "response code -- > ${response.code()}")
                if (response.code() == 200) {
                    LogUtils.d(TAG, "result size -- > ${response.body()?.data?.size}")
                    //处理列表数据
                    handlerContentResult(response.body())
                }
            }

        })
    }

    /**
     * 处理列表内容结果，前三个作为轮播图内容，后面的做为列表内容
     */
    private fun handlerContentResult(result: ContentItem?) {
        Collections.shuffle(result?.data)
        //前5个内容，用于做轮播图
        //后面的内容作列表内容
        if (result?.data?.size!! > 5) {
            val looper = result.data.subList(0, 5)
            val contentList = result.data.subList(5, result.data.size)
            for (callback in callbacks) {
                callback.onContentListUploaded(contentList)
                callback.onLooperLoaded(looper)
            }
        }
    }

    override fun loadMoreData() {

    }

    override fun registerCallback(callback: IHomeCallback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }

    override fun unregisterCallback(callback: IHomeCallback) {
        callbacks.remove(callback)
    }
}