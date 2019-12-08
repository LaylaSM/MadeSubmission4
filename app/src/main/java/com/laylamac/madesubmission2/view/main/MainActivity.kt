package com.laylamac.madesubmission2.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.base.BaseAppCompatActivity
import com.laylamac.madesubmission2.view.favorite.FavoriteFragment
import com.laylamac.madesubmission2.view.movie.MovieFragment
import com.laylamac.madesubmission2.view.tvshow.TvShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAppCompatActivity() {

    private var pageMovie: Fragment? = MovieFragment()
    private var pageTvShow: Fragment? = TvShowFragment()
    private var pageFavorite: Fragment? = FavoriteFragment()

    private var selectedPage = "Movie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarTitle(selectedPage)

        pageMovie = MovieFragment()
        pageTvShow = TvShowFragment()
        pageFavorite = FavoriteFragment()

        main_bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.movie_bottom_navigation_menu -> {
                    selectedPage = "Movie"
                    pageMovie = MovieFragment()
                    setActionBarTitle(selectedPage)
                    loadPage(pageMovie!!)
                }
                R.id.tv_bottom_navigation_menu -> {
                    selectedPage = "TvShow"
                    pageTvShow = TvShowFragment()
                    setActionBarTitle(selectedPage)
                    loadPage(pageTvShow!!)
                }
                R.id.fav_bottom_navigation_menu -> {
                    selectedPage = "Favorite"
                    pageFavorite = FavoriteFragment()
                    setActionBarTitle(selectedPage)
                    loadPage(pageFavorite!!)
                }
                else -> {
                    selectedPage = "Movie"
                    pageMovie = MovieFragment()
                    setActionBarTitle(selectedPage)
                    loadPage(MovieFragment())
                }
            }
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, pageMovie!!).commit()
            setActionBarTitle(selectedPage)
        } else {
            selectedPage = savedInstanceState.getString(KEY_TITLE)!!
            when {
                selectedPage.equals("Movie", ignoreCase = true) -> {
                    pageMovie = supportFragmentManager.getFragment(savedInstanceState, KEY_MOVIE_FRAGMENT)
                    loadPage(pageMovie!!)
                }
                selectedPage.equals("TvShow", ignoreCase = true) -> {
                    pageTvShow = supportFragmentManager.getFragment(savedInstanceState, KEY_TVSHOW_FRAGMENT)
                    loadPage(pageTvShow!!)
                }
                else -> {
                    pageFavorite = supportFragmentManager.getFragment(savedInstanceState, KEY_FAVORITE_FRAGMENT)
                    loadPage(pageFavorite!!)

                }
            }

            setActionBarTitle(selectedPage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val intent = Intent(ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadPage(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment)
            .commit()
        return true
    }


    // To maintain data that has been loaded when there is a change in orientation
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, selectedPage)
        if (pageMovie?.isAdded!!) {
            supportFragmentManager.putFragment(outState, KEY_MOVIE_FRAGMENT, pageMovie!!)
        }

        if (pageTvShow?.isAdded!!) {
            supportFragmentManager.putFragment(outState, KEY_TVSHOW_FRAGMENT, pageTvShow!!)
        }
        if (pageFavorite?.isAdded!!){
            supportFragmentManager.putFragment(outState, KEY_FAVORITE_FRAGMENT, pageFavorite!!)

        }
        super.onSaveInstanceState(outState)
    }



    @Suppress("SameParameterValue")
    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = title
        }

    }
}
