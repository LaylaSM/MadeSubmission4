package com.laylamac.madesubmission2.view.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.laylamac.madesubmission2.BuildConfig
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.db.MovieDB
import com.laylamac.madesubmission2.db.TvShowDB
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.model.TvShowMdl
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    val EXTRA_TYPE = "extra_type"
    val EXTRA_MOVIE_ID = "extra_movie_id"
    val EXTRA_TV_SHOW_ID = "extra_tv_show_id"

    private lateinit var mMovie: MovieMdl
    private lateinit var mMovieDB: MovieDB
    private lateinit var mTvShow: TvShowMdl
    private lateinit var mTvShowDB: TvShowDB
    private lateinit var type: String
    var isFavorite = false
    var isLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setActionBarTitle("Details")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pb_detail_activity.visibility = View.VISIBLE
        type = intent.getStringExtra(EXTRA_TYPE)!!

        mMovieDB = MovieDB.getInstance(applicationContext)
        mMovieDB.open()

        mTvShowDB = TvShowDB.getInstance(applicationContext)
        mTvShowDB.open()

        val params = RequestParams()
        params.put("api_key", BuildConfig.TMDB_API_KEY)
        params.put("language", "en-US")
        val client = AsyncHttpClient()

        when (type) {
            "movie" -> {
                val id = intent.getStringExtra(EXTRA_MOVIE_ID)
                val url = BuildConfig.URL_DETAIL_MOVIE + "/$id"
                client.get(url, params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<Header>?,
                        response: JSONObject?
                    ) {
                        try {
                            mMovie = MovieMdl(response as JSONObject)
                            tv_title_detail.text = mMovie.title
                            Glide.with(applicationContext)
                                .load("https://image.tmdb.org/t/p/w185" + mMovie.poster)
                                .into(iv_poster_detail)
                            tv_release_date_detail.text = mMovie.release
                            tv_desc_detail.text = mMovie.description


                        } catch (e: Exception) {
                            Log.d("error", e.message!!)
                        }
                        isLoad = true
                        pb_detail_activity.visibility = View.GONE
                        isFavorite = isFavorites()
                        invalidateOptionsMenu()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        throwable: Throwable?,
                        errorResponse: JSONObject?
                    ) {
                        Toast.makeText(
                            applicationContext,
                            errorResponse?.getString("status_message"),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })
            }
            "tv" -> {
                val id = intent.getStringExtra(EXTRA_TV_SHOW_ID)
                val url = BuildConfig.URL_DETAIL_TV_SHOW + "/$id"
                client.get(url, params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        try {
                            mTvShow = TvShowMdl(response as JSONObject)
                            tv_title_detail.text = mTvShow.title
                            Glide.with(applicationContext)
                                .load("https://image.tmdb.org/t/p/w185" + mTvShow.poster)
                                .into(iv_poster_detail)
                            tv_release_date_detail.text = mTvShow.release
                            tv_desc_detail.text = mTvShow.description

                        } catch (e: Exception) {
                            Log.d("error", e.message!!)
                        }
                        isLoad = true
                        pb_detail_activity.visibility = View.GONE
                        isFavorite = isFavorites()
                        invalidateOptionsMenu()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        throwable: Throwable?,
                        error: JSONObject?
                    ) {
                        Toast.makeText(applicationContext, error?.getString("status_message"), Toast.LENGTH_SHORT)
                            .show()
                    }

                })
            }
            "movie_favorite" -> {
                mMovie = intent.getParcelableExtra("fav_movie")!!
                tv_title_detail.text = mMovie.title
                Glide.with(applicationContext)
                    .load("https://image.tmdb.org/t/p/w185" + mMovie.poster)
                    .into(iv_poster_detail)
                tv_release_date_detail.text = mMovie.release
                tv_desc_detail.text = mMovie.description

                pb_detail_activity.visibility = View.GONE
                isLoad = true
                isFavorite = isFavorites()
                invalidateOptionsMenu()
            }
            "tv_show_favorite" -> {
                mTvShow = intent.getParcelableExtra("fav_tv_show")!!
                tv_title_detail.text = mTvShow.title
                Glide.with(applicationContext)
                    .load("https://image.tmdb.org/t/p/w185" + mTvShow.poster)
                    .into(iv_poster_detail)
                tv_release_date_detail.text = mTvShow.release
                tv_desc_detail.text = mTvShow.description

                isLoad = true
                pb_detail_activity.visibility = View.GONE
                isFavorite = isFavorites()
                invalidateOptionsMenu()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_add_to_fav -> {
                if (isFavorite) {
                    item.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
                    if (type == "movie" || type == "movie_favorite") {
                        val result = mMovieDB.deleteMovie(mMovie.id)
                        if (result > 0) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.cancel_favorite),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isFavorite = isFavorites()
                        }

                    } else if (type == "tv" || type == "tv_show_favorite") {
                        val result = mTvShowDB.deleteTvShow(mTvShow.id!!)
                        if (result > 0) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.cancel_favorite),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isFavorite = isFavorites()
                        }
                    }
                } else {
                    item.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
                    if (type == "movie" || type == "movie_favorite") {
                        val result = mMovieDB.insertMovie(mMovie)
                        if (result > 0) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.add_favorite),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isFavorite = isFavorites()
                        }
                    } else if (type == "tv" || type == "tv_show_favorite") {
                        val result = mTvShowDB.insertTvShow(mTvShow)
                        if (result > 0) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.add_favorite),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isFavorite = isFavorites()

                        }
                    }

                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun isFavorites(): Boolean {
        if (type == "movie" || type == "movie_favorite") {
            if (mMovieDB.isFavoriteMovie(mMovie.id)) {
                return true
            }
        } else if (type == "tv" || type == "tv_show_favorite") {
            if (mTvShowDB.isFavoriteTvShow(mTvShow.id!!)) {
                return true
            }
        }
        return false
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (isLoad) {
            menu?.getItem(0)?.isVisible = true
        }
        if (isFavorite) {
            menu?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)

        } else {
            menu?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    @Suppress("SameParameterValue")
    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = title
        }

    }
}


