package com.laylamac.madesubmission2.db

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {
    var TABLE_MOVIE = "movie"
    var TABLE_TV_SHOW = "tv_show"
    val AUTHOR = "com.laylamac.madesubmission2"
    private val SCHEME = "content"

    internal class MovieColumns : BaseColumns {
        companion object {
            var ID = "id"
            var TITLE = "title"
            var POSTER = "poster"
            var RELEASE_DATE = "release_date"
            var DESCRIPTION = "description"
        }
        val CONTENT_URI = Uri.Builder().scheme(DatabaseContract().SCHEME)
            .authority(DatabaseContract().AUTHOR)
            .appendPath(DatabaseContract().TABLE_MOVIE)
            .build()

    }

    fun getColumnString (cursor : Cursor, columnName : String): String{
        return cursor.getString(cursor.getColumnIndex(columnName))
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
