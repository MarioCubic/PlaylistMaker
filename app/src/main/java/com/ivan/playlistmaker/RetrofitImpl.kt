package com.ivan.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImpl {
    private val iTunesUrl = "https://itunes.apple.com/"
    fun getRetrofitImpl(): ItunesAPI {
        return Retrofit.Builder()
            .baseUrl(iTunesUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }


}