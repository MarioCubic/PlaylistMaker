package com.ivan.playlistmaker.search.data

import com.ivan.playlistmaker.data.dto.TrackResponse
import com.ivan.playlistmaker.search.data.network.RetrofitImpl
import com.ivan.playlistmaker.search.domain.api.TracksRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksRepositoryImpl : TracksRepository {
    private val serverDataSource = RetrofitImpl().getRetrofitImpl()

    override fun searchTracks(
        expression: String,
        onResult: (TrackResponse) -> Unit,
        onError: () -> Unit,
        onNoInternet: () -> Unit
    ) {
        serverDataSource.search(expression).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse?>,
                response: Response<TrackResponse?>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()?.resultCount != 0) {
                    onResult(response.body()!!)
                } else {
                    onError()
                }
            }

            override fun onFailure(call: Call<TrackResponse?>, t: Throwable) {
                onNoInternet()
            }
        })
    }
}
