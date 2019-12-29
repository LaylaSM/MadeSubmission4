package com.laylamac.madesubmission2.view.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.base.BaseAppCompatActivity
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.model.TvShowMdl
import com.laylamac.madesubmission2.view.detail.DetailActivity
import com.laylamac.madesubmission2.view.movie.MovieAdapter
import com.laylamac.madesubmission2.view.setting.SettingsActivity
import com.laylamac.madesubmission2.view.tvshow.TvShowAdapter
import com.laylamac.madesubmission2.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseAppCompatActivity(),MovieAdapter.OnItemClicked, TvShowAdapter.OnItemClicked {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvShowAdapter: TvShowAdapter
    private lateinit var query: String
    private val movieList :  MutableList<MovieMdl> = mutableListOf()
    private val tvList :  MutableList<TvShowMdl> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        movieAdapter = MovieAdapter(this, movieList,this)
        tvShowAdapter = TvShowAdapter(this, tvList, this)

        query = intent.getStringExtra(EXTRA_QUERY)!!
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.movies.observe(this, getMovie)
        val adapter = ArrayAdapter<String>(
            applicationContext, android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.filter_search)
        )
        spinner_search_activity.adapter = adapter
        spinner_search_activity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    pb_search_activity.visibility = View.VISIBLE
                    tv_null_search_activity.visibility = View.GONE
                    if (spinner_search_activity.selectedItemPosition == 0) {
                        searchMovie()
                    } else if (spinner_search_activity.selectedItemPosition == 1) {
                        searchTvShow()
                    }
                }

            }
    }

    private fun searchTvShow() {
        movieViewModel.tvShow.observe(this@SearchActivity, getTvShow)
        movieViewModel.searchMovie(TYPE_TVSHOW, query)
        tv_null_search_activity.visibility = View.GONE
    }

    private val getTvShow = Observer<ArrayList<TvShowMdl>> { tvShow ->
        if (tvShow != null) {
            tvShowAdapter = TvShowAdapter(
                applicationContext, tvShow, object : TvShowAdapter.OnItemClicked {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        intent.putExtra(DetailActivity().EXTRA_TV_SHOW_ID, tvShow[position].id)
                        intent.putExtra(DetailActivity().EXTRA_TYPE, TYPE_TVSHOW)
                        startActivity(intent)
                    }
                })

            rv_search_activity.apply {
                adapter = tvShowAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            pb_search_activity.visibility = View.GONE
        } else {
            tvShowAdapter.listTv.clear()
            pb_search_activity.visibility = View.GONE
            tv_null_search_activity.visibility = View.VISIBLE
        }
    }


    private fun searchMovie() {
        movieViewModel.movies.observe(this@SearchActivity, getMovie)
        movieViewModel.searchMovie(TYPE_MOVIE, query)
        tv_null_search_activity.visibility = View.GONE
    }

    private val getMovie = Observer<ArrayList<MovieMdl>> { movie ->
        if (movie != null) {
            movieAdapter = MovieAdapter(
                applicationContext,
                movie, object : MovieAdapter.OnItemClicked {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        intent.putExtra(DetailActivity().EXTRA_MOVIE_ID, movie[position].id)
                        intent.putExtra(DetailActivity().EXTRA_TYPE, TYPE_MOVIE)
                        startActivity(intent)
                    }
                })
            rv_search_activity.apply {
                adapter = movieAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            pb_search_activity.visibility = View.GONE
        } else {
            movieAdapter.listMovie.clear()
            pb_search_activity.visibility = View.GONE
            tv_null_search_activity.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu?.findItem(R.id.action_search_main)?.actionView) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                this@SearchActivity.query = query.toString()
                pb_search_activity.visibility = View.VISIBLE

                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                if (spinner_search_activity.selectedItemPosition == 0) {
                    searchMovie()
                } else if (spinner_search_activity.selectedItemPosition == 1) {
                    searchTvShow()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)

    }

    override fun onItemClick(position: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_settings) {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item!!)
    }

}
