package com.sunofbeaches.ticketunion.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.sunofbeaches.ticketunion.base.BaseApplication
import com.sunofbeaches.ticketunion.model.domain.JsonCacheItem

class JsonCacheUtil {

    private var sharedPreferences: SharedPreferences? = null

    private constructor(context: Context) {
        sharedPreferences = context.getSharedPreferences(CACHE_SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 保存缓存，不过时
     */
    fun saveCache(key: String, jsonObject: Any?) {
        val edit = sharedPreferences?.edit()
        var json = Gson().toJson(jsonObject)
        val jsonCacheItem = JsonCacheItem(-1L, json)
        val toJson = Gson().toJson(jsonCacheItem)
        edit?.putString(key, toJson)
        edit?.commit()
    }

    /**
     * 保存缓存，有时长，单位为毫秒
     */
    fun saveCache(key: String, jsonObject: Any?, duration: Long) {
        val edit = sharedPreferences?.edit()
        val deprecatedTime = System.currentTimeMillis() + duration
        val json = Gson().toJson(jsonObject)
        val jsonCacheItem = JsonCacheItem(deprecatedTime, json)
        val toJson = Gson().toJson(jsonCacheItem)
        edit?.putString(key, toJson)
        edit?.commit()
    }

    fun <T> getCache(key: String, classOfT: Class<T>): T? {
        val json = sharedPreferences?.getString(key, "")
        LogUtils.d(this, "json -- > $json")
        if (!TextUtils.isEmpty(json)) {
            val gson = Gson()
            val jsonCache: JsonCacheItem =
                gson.fromJson<JsonCacheItem>(json, JsonCacheItem::class.java)
            //判断时间
            if (jsonCache.duration != -1L && System.currentTimeMillis() - jsonCache.duration >= 0) {
                return null
            }
            return gson.fromJson<T>(jsonCache.json, classOfT)
        }
        return null
    }

    /**
     * 清
     */
    fun delCache(key: String) {
        sharedPreferences?.edit()?.remove(key)?.commit()
    }

    companion object {

        const val CACHE_SP_NAME = "json_cache.sp"

        private var sInstance: JsonCacheUtil? = null
        fun getInstance(): JsonCacheUtil {
            if (sInstance == null) {
                synchronized(JsonCacheUtil::class.java) {
                    if (sInstance == null) {
                        sInstance = JsonCacheUtil(BaseApplication.getContext())
                    }
                }
            }
            return sInstance!!
        }
    }
}