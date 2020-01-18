package com.sunofbeaches.ticketunion.presenter.impl

import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.model.domain.CategoryPageItem
import com.sunofbeaches.ticketunion.presenter.ICategoryPagePresenter
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.UrlUtils
import com.sunofbeaches.ticketunion.view.IHomeCategoryPageCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class CategoryPagePresenterImpl :
    ICategoryPagePresenter , BasePresenter<IHomeCategoryPageCallback>() {

    override fun reload(categoryId: Int) {
        getCategoryContentById(categoryId)
    }

    override fun reload() {

    }

    private val pageInfo: HashMap<Int, Int> = HashMap()


    private var isLoadingMore = false

    override fun loaderMore(categoryId: Int) {
        if (isLoadingMore) {
            return
        }
        //拿到当前的页码，然后++
        isLoadingMore = true
        //请求以后发起请求
        val lastPage = pageInfo[categoryId]
        if (lastPage != null) {
            var currentPage = lastPage + 1
            //加载更多内容
            pageInfo[categoryId] = currentPage
            val task = createTask(categoryId, currentPage)
            task?.enqueue(object : Callback<CategoryPageItem> {
                override fun onFailure(call: Call<CategoryPageItem>, t: Throwable) {
                    //更新失败，这个可不能干掉原来的数据了
                    handlerLoaderMoreError(categoryId)
                    //失败了，让页码复原
                    pageInfo[categoryId] = currentPage - 1
                    isLoadingMore = false
                }

                override fun onResponse(
                    call: Call<CategoryPageItem>,
                    response: Response<CategoryPageItem>
                ) {
                    isLoadingMore = false
                    //数据成功不能直接替换了，要添加到底部
                    val code = response.code()
                    LogUtils.d(this, "loader more result code -- > $code")
                    if (HttpURLConnection.HTTP_OK == code) {
                        //其实这里也有一个状态码，此项目基本上都是成功的，跟账号权限之类的没有关系
                        handlerLoaderMoreSuccess(response.body(), categoryId)
                    } else {
                        //只要请求不回来，不管是服务器的问题还是什么问题
                        //都怪网络不好就对了，除非产品经理要求特别处理
                        handlerLoaderMoreError(categoryId)
                    }
                }

            })
        }
    }

    /**
     * 处理加载更多的结果
     */
    private fun handlerLoaderMoreSuccess(result: CategoryPageItem?, categoryId: Int) {
        if (result == null || result.data.isEmpty()) {
            for (callback in callbacks) {
                callback.onLoaderMoreEmpty(categoryId)
            }
        } else {
            for (callback in callbacks) {
                callback.onLoaderMoreLoaded(result, categoryId)
            }
        }
    }

    private fun handlerLoaderMoreError(categoryId: Int) {
        for (callback in callbacks) {
            callback.onLoaderMoreError(categoryId)
        }
    }

    /**
     * 根据分类的id获取分类内容
     */
    override fun getCategoryContentById(categoryId: Int) {
        onLoading(categoryId)

        pageInfo[categoryId] = DEFAULT_PAGE
        //设置当前页码
        val task = createTask(categoryId, DEFAULT_PAGE)
        task?.enqueue(object : Callback<CategoryPageItem> {
            override fun onFailure(call: Call<CategoryPageItem>, t: Throwable) {
                LogUtils.d(this, "error -- > $t")
                handlerFailure(categoryId)
            }

            override fun onResponse(
                call: Call<CategoryPageItem>,
                response: Response<CategoryPageItem>
            ) {
                val code = response.code()
                LogUtils.d(this, "code -- > $code")
                if (HttpURLConnection.HTTP_OK == code) {
                    handlerContentResponse(response.body(), categoryId)
                }
            }

        })
    }

    private fun createTask(categoryId: Int, targetPage: Int): Call<CategoryPageItem>? {

        LogUtils.d(this, "target category id is $categoryId")
        val url = UrlUtils.getDiscoveryContentUrl(categoryId, targetPage)
        LogUtils.d(this, "target url is -- > $url")
        return mApi?.getContentListByMaterialId(url)
    }

    private fun onLoading(categoryId: Int) {
        for (callback in callbacks) {
            callback.onLoading(categoryId)
        }
    }

    private fun handlerContentResponse(
        result: CategoryPageItem?,
        categoryId: Int
    ) {
        if (result == null || result.data.isEmpty()) {
            for (callback in callbacks) {
                callback.onEmpty(categoryId)
            }
        } else {
            //切割数据，后5个用于作轮播图，后面的才是列表数据
            val data = result.data
            val subList = data.subList(data.size - 5, data.size)
            for (callback in callbacks) {
                callback.onLooperDataLoaded(subList, categoryId)
                callback.onContentLoaded(result, categoryId)
            }
        }

    }

    private fun handlerFailure(categoryId: Int) {
        for (callback in callbacks) {
            callback.onError(categoryId)
        }
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }

}