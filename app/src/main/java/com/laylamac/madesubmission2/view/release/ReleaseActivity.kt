package com.laylamac.madesubmission2.view.release

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.view.detail.DetailActivity
import com.laylamac.madesubmission2.view.movie.MovieAdapter
import kotlinx.android.synthetic.main.activity_release.*

class ReleaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_release)
        supportActionBar?.title = "Release Today"

        pb_release_activity.visibility = View.VISIBLE
        if (intent.getSerializableExtra("movieList") != null) {
            showUpMovies()
        }

    }

    private fun showUpMovies() {
        val list = intent.getSerializableExtra("movieList") as ArrayList<MovieMdl>
        rv_release_activity.apply {
            adapter = MovieAdapter(applicationContext, list, object : MovieAdapter.OnItemClicked {
                override fun onItemClick(position: Int) {
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                    intent.putExtra(DetailActivity().EXTRA_MOVIE_ID, list[position].id)
                    intent.putExtra(DetailActivity().EXTRA_TYPE, "movie")
                    startActivity(intent)
                }

            })
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }

        pb_release_activity.visibility = View.GONE
    }
}
