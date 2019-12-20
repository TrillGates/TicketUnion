package com.sunofbeaches.ticketunion.model

import com.sunofbeaches.ticketunion.domain.ContentItem
import com.sunofbeaches.ticketunion.domain.MainCategoryItem
import com.sunofbeaches.ticketunion.domain.TickerResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

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
    fun getContentListByMaterialId(@Url url: String): Call<ContentItem>

    @POST("tpwd")
    fun getTicketByUrl(@Query("url") url: String): Call<TickerResult>


}