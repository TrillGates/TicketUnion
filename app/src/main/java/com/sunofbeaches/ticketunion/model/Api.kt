package com.sunofbeaches.ticketunion.model

import com.sunofbeaches.ticketunion.model.domain.*
import retrofit2.Call
import retrofit2.http.*

interface Api {
    /**
     * 获取到分类
     */
    @GET("discovery/categories")
    fun getCategories(): Call<MainCategoryItem>

    /**
     * 根据分类，获取到内容
     */
    @GET
    fun getContentListByMaterialId(@Url url: String): Call<CategoryPageItem>


    /**
     * 获取淘口令
     */
    @POST("tpwd")
    fun getTicketByUrl(@Body body: TicketRequestItem): Call<TickerResult>

    /**
     * 获取特惠内容
     */
    @GET
    fun getOnSellGoods(@Url url: String): Call<OnSellResultItem>

    /**
     * 获取精选的类型
     */
    @GET("recommend/categories")
    fun loadPickTypeList(): Call<PickTypeItem>

    /**
     * 获取精选内容
     */
    @GET
    fun getPickContentList(@Url url: String): Call<PickContentItem>

    @GET("search")
    fun doSearch(@Query("page") page: Int, @Query("keyword") keyword: String): Call<SearchResult>

    @GET("search/recommend")
    fun getSearchRecommend(): Call<SearchRecommend>

}