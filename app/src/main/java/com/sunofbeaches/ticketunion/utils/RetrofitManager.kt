package com.sunofbeaches.ticketunion.utils

import com.sunofbeaches.ticketunion.model.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {

    private var retrofit: Retrofit? = null

    private constructor() {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    companion object {
        const val BASE_URL: String = "https://www.sunofbeach.net/shop/api/"
       // const val BASE_URL: String = "http://10.0.2.2:50200/shop/api/"
        var instance: RetrofitManager? = null
            get() {
                if (field == null) {
                    field = RetrofitManager()
                }
                return field
            }

        fun get(): RetrofitManager {
            return instance!!
        }
    }

    fun getApi(): Api {
        return retrofit?.create(Api::class.java)!!
    }
}