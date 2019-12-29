package com.laylamac.madesubmission2.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.base.BaseAppCompatActivity
import com.laylamac.madesubmission2.utils.AlarmReceiver
import com.laylamac.madesubmission2.utils.SharedPrefManager
import com.laylamac.madesubmission2.view.favorite.FavoriteFragment
import com.laylamac.madesubmission2.view.movie.MovieFragment
import com.laylamac.madesubmission2.view.release.ReleaseActivity
import com.laylamac.madesubmission2.view.search.SearchActivity
import com.laylamac.madesubmission2.view.setting.SettingsActivity
import com.laylamac.madesubmission2.view.tvshow.TvShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAppCompatActivity() {

    private var pageMovie: Fragment? = MovieFragment()
    private var pageTvShow: Fragment? = TvShowFragment()
    private var pageFavorite: Fragment? = FavoriteFragment()
    private val alarmReceiver = AlarmReceiver()

    private var selectedPage = "Movie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarTitle(selectedPage)

        pageMovie = MovieFragment()
        pageTvShow = TvShowFragment()
        pageFavorite = FavoriteFragment()

        val sharePref = SharedPrefManager(applicationContext).getInstance(applicationContext)
        if (sharePref.checkInit() == 0){
            sharePref.setDailyRemind(true)
            sharePref.setReleaseRemind(true)
            alarmReceiver.setRepeatAlarm(applicationContext, AlarmReceiver().TYPE_DAILY,"09:00","It's time!, Go to looking what's new!")
            alarmReceiver.setRepeatAlarm(applicationContext, AlarmReceiver().TYPE_RELEASE, "10:00","Click to know's whats new body!")
        }

        if (intent.extras != null){
            if (EXTRA_TYPE == "release"){
                val intent = Intent(applicationContext, ReleaseActivity::class.java)
                intent.putExtra("movieList", this.intent.getSerializableExtra("movieList"))
                startActivity(intent)
            }
        }

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
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu?.findItem(R.id.action_search_main)?.actionView) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra(SearchActivity().EXTRA_QUERY, query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            //val intent = Intent(ACTION_LOCALE_SETTINGS)
           // startActivity(intent)
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
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
