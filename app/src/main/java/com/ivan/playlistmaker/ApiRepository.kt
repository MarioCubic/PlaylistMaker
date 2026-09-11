package com.ivan.playlistmaker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    private val serverDataSource = RetrofitImpl().getRetrofitImpl()

    fun fetchData(track: String, onResult: (TrackResponse) -> Unit, onError: () -> Unit, onNoInternet: () -> Unit)  {
        serverDataSource.search(track).enqueue(object : Callback<TrackResponse> {
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