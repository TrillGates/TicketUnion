package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.model.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.presenter.HomePagePresenter
import com.sunofbeaches.ticketunion.utils.Constants
import com.sunofbeaches.ticketunion.utils.JsonCacheUtil
import com.sunofbeaches.ticketunion.utils.LogUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by TrillGates on 2019-12-17.
 * God bless my code!
 */
class HomePagePresenterImpl :
    HomePagePresenter() {

    override fun reload() {
        loadCategories()
    }


    override fun loadCategories() {
        onLoading()
        LogUtils.d(this, "loadCategories...")
        //先从缓存中拿
        val categoriesCache =
            JsonCacheUtil.getInstance().getCache(
                Constants.KEY_INDEX_CATEGORIES_DATA, MainCategoryItem::class.java
            )
        //println(categoriesCache)
        if (categoriesCache != null) {
            LogUtils.d(this, "categories from cache...")
            updateCategories(categoriesCache)
        }

        //加载数据
        val categories = mApi?.getCategories()
        categories?.enqueue(object : Callback<MainCategoryItem> {
            override fun onFailure(call: Call<MainCategoryItem>, t: Throwable) {
                LogUtils.d(this, "categories -> onFailure...${t}")
            }

            override fun onResponse(
                call: Call<MainCategoryItem>,
                response: Response<MainCategoryItem>
            ) {
                LogUtils.d(this, "get categories success.")
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val body = response.body()

                    //缓存起来
                    JsonCacheUtil.getInstance().saveCache(
                        Constants.KEY_INDEX_CATEGORIES_DATA, body, 1000 * 60 * 60
                    )
                    //更新UI
                    updateCategories(body)
                }
            }

        })
    }

    private fun onLoading() {
        for (callback in callbacks) {
            callback.onLoading()
        }
    }

    private fun updateCategories(result: MainCategoryItem?) {
        for (callback in callbacks) {
            callback.onCategoriesLoaded(result)
        }
    }


}