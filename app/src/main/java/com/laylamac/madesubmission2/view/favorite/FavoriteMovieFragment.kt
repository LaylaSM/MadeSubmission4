package com.laylamac.madesubmission2.view.favorite


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.db.MovieDB
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.view.detail.DetailActivity
import com.laylamac.madesubmission2.view.movie.MovieAdapter
import kotlinx.android.synthetic.main.fragment_favorite_movie.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    lateinit var mMovieDB: MovieDB
    lateinit var mFavoriteMovieList: ArrayList<MovieMdl>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMovieDB = MovieDB.getInstance(context!!)
        mMovieDB.open()

        mFavoriteMovieList = mMovieDB.getDataMovies()

        favorite_movie_recycler.apply {
            adapter =
                MovieAdapter(context, mFavoriteMovieList, object : MovieAdapter.OnItemClicked {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("extra_type", "movie_favorite")
                        intent.putExtra("fav_movie", mFavoriteMovieList[position])
                        startActivity(intent)
                    }
                })
            val llm = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            llm.stackFromEnd = true
            layoutManager = llm
        }
        favorite_movie_progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        mFavoriteMovieList.clear()
        mFavoriteMovieList.addAll(mMovieDB.getDataMovies())
        favorite_movie_recycler.adapter?.notifyDataSetChanged()
    }
}
