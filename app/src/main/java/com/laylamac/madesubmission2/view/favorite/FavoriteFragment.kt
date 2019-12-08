package com.laylamac.madesubmission2.view.favorite


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.view.TabAdapter
import com.laylamac.madesubmission2.view.movie.MovieFragment
import com.laylamac.madesubmission2.view.tvshow.TvShowFragment
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {
    var KEY_TITLE = "title"
    var KEY_MOVIE_FRAGMENT = "MovieFragment"
    var KEY_TVSHOW_FRAGMENT = "TvFragment"

    private var pageMovie: Fragment? = MovieFragment()
    private var pageTvShow: Fragment? = TvShowFragment()
    private var title = "Home"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    /*  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          val adapter = TabAdapter(activity!!.supportFragmentManager)
          adapter.addFragment(FavoriteMovieFragment(), getString(R.string.movies))
          adapter.addFragment(FavoriteTvShowFragment(), getString(R.string.tv_shows))

          favorite_view_pager.adapter = adapter
          favorite_lab_layout.setupWithViewPager(favorite_view_pager)

      }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageMovie = FavoriteMovieFragment()
        pageTvShow = FavoriteTvShowFragment()
        val adapter = TabAdapter(activity!!.supportFragmentManager)

        adapter.addFragment(pageMovie, getString(R.string.movies))
        adapter.addFragment(pageTvShow, getString(R.string.tv_shows))

        favorite_view_pager.adapter = adapter
        favorite_lab_layout.setupWithViewPager(favorite_view_pager)

        /*if (savedInstanceState == null){
            val adapter = TabAdapter(activity!!.supportFragmentManager)
            adapter.addFragment(pageMovie, getString(R.string.movies))
            adapter.addFragment(pageTvShow, getString(R.string.tv_shows))

            favorite_view_pager.adapter = adapter
            favorite_lab_layout.setupWithViewPager(favorite_view_pager)
        } else {
        //    title = savedInstanceState.getString(KEY_TITLE)!!

            val adapter = TabAdapter(activity!!.supportFragmentManager)
            *//*pageMovie = adapter.getFragment(savedInstanceState, KEY_MOVIE_FRAGMENT)
            pageTvShow = adapter.getFragment(savedInstanceState, KEY_TVSHOW_FRAGMENT)*//*

            adapter.addFragment(pageMovie, "Moviesss")
            adapter.addFragment(pageTvShow,"TvShowsss")
            favorite_view_pager.adapter = adapter
            favorite_lab_layout.setupWithViewPager(favorite_view_pager)

        }*/

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, title)
        super.onSaveInstanceState(outState)
    }


}
