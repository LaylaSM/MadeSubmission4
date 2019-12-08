package com.laylamac.madesubmission2.view.movie


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.base.BaseFragment
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.view.detail.DetailActivity
import com.laylamac.madesubmission2.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : BaseFragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var mAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.movies.observe(this, getMovies)
        movieViewModel.setMoviesTvShows("movie")

        pb_movie_fragment.visibility = View.VISIBLE


    }

    private val getMovies = Observer<ArrayList<MovieMdl>> { movie ->
        if (movie != null) {
            mAdapter = MovieAdapter(context!!, movie, object : MovieAdapter.OnItemClicked {
                override fun onItemClick(position: Int) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("extra_movie_id", movie[position].id)
                    intent.putExtra("extra_type", "movie")
                    startActivity(intent)
                }
            })

            movie_rv.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            pb_movie_fragment.visibility = View.GONE
        }
    }


}
