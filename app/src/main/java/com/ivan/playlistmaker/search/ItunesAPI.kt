package com.ivan.playlistmaker.search

import com.ivan.playlistmaker.model.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term")text: String): Call<TrackResponse>
}