package com.ivan.playlistmaker.search.data.network

import com.ivan.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term")text: String): Call<TrackResponse>
}
