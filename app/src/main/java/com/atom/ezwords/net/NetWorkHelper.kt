package com.atom.ezwords.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkHelper {

    /**
     * okhttp实例
     *  请求头签名加密
     * @return OkHttpClient
     */
    private fun provideOkHttpClient(): OkHttpClient {
        //初始化okhttp
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(NetLogInterceptor())
        return builder.build()
    }


    /**
     * 获取Retrofit实例
     * @return Retrofit
     */
    fun provideRetrofit(): Retrofit {
        val okHttpClient = provideOkHttpClient()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://reciteword.youdao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
