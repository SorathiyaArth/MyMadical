package com.test.mymadical

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Retroclient {

    private val APP_CONFIG_LINK = "https://arthsorathiya.000webhostapp.com/"
    private var singleTonRetrogit: Retrofit? = null
    private var singleTonClient: OkHttpClient.Builder? = null


    fun getSingleTonClient(): Retrofit? {
        if (singleTonClient == null) {
            singleTonClient = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            singleTonClient!!.connectTimeout(2, TimeUnit.MINUTES)
            singleTonClient!!.readTimeout(2, TimeUnit.MINUTES)
            singleTonClient!!.addInterceptor(loggingInterceptor)
        }
        if (singleTonRetrogit == null) {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            singleTonRetrogit = Retrofit.Builder()
                .baseUrl(APP_CONFIG_LINK)
                .client(singleTonClient!!.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return singleTonRetrogit
    }

}