package com.laylamac.madesubmission2.db

import android.provider.BaseColumns

class DatabaseContract {
    var TABLE_MOVIE = "movie"
    var TABLE_TV_SHOW = "tv_show"

    internal class MovieColumns : BaseColumns {
        companion object {
            var ID = "id"
            var TITLE = "title"
            var POSTER = "poster"
            var RELEASE_DATE = "release_date"
            var DESCRIPTION = "description"
        }
    }

    internal class TVShowColumns : BaseColumns {
        companion object {
            var ID = "id"
            var TITLE = "title"
            var POSTER = "poster"
            var FIRST_AIR_DATE = "first_air_date"
            var DESCRIPTION = "description"
        }
    }

}
