package com.laylamac.madesubmission2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laylamac.madesubmission2.BuildConfig
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.model.TvShowMdl
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*


class MovieViewModel : ViewModel() {
    private val listMovies = MutableLiveData<ArrayList<MovieMdl>>()
    private var listTvShow = MutableLiveData<ArrayList<TvShowMdl>>()

    internal val movies: LiveData<ArrayList<MovieMdl>>
        get() = listMovies

    internal val tvShow: LiveData<ArrayList<TvShowMdl>>
        get() = listTvShow

    internal fun setMoviesTvShows(type: String) {
        val listItemMovie = ArrayList<MovieMdl>()
        val listItemTvShow = ArrayList<TvShowMdl>()

        val params = RequestParams()
        params.put("api_key", BuildConfig.TMDB_API_KEY)
        params.put("language", "en-US")
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/$type"
        client.get(url, params, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        if (type == "movie") {
                            val movie = list.getJSONObject(i)
                            val itemMovie = MovieMdl(movie)
                            listItemMovie.add(itemMovie)
                            listMovies.postValue(listItemMovie)
                        } else if (type == "tv") {
                            val tvShow = list.getJSONObject(i)
                            val itemTvShow = TvShowMdl(tvShow)
                            listItemTvShow.add(itemTvShow)
                            listTvShow.postValue(listItemTvShow)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("error", e.message!!)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                Log.d("Failure", error?.message!!)
            }

        })
    }

}
