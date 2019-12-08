package com.laylamac.madesubmission2.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseAppCompatActivity : AppCompatActivity() {

    var KEY_TITLE = "title"
    var KEY_MOVIE_FRAGMENT = "MovieFragment"
    var KEY_TVSHOW_FRAGMENT = "TvFragment"
    var KEY_FAVORITE_FRAGMENT = "FavoriteFragment"

}