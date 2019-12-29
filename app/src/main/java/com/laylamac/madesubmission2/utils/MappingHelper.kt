package com.laylamac.madesubmission2.utils

import android.database.Cursor
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.DESCRIPTION
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.ID
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.RELEASE_DATE
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.TITLE

class MappingHelper {

    fun mapCursorToArrayList (movieCursor: Cursor): ArrayList<MovieMdl>{
        val listMovie = ArrayList<MovieMdl>()
        while (movieCursor.moveToNext()){
            val id = movieCursor.getString(movieCursor.getColumnIndexOrThrow(ID))
            val title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE))
            val poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER))
            val release = movieCursor.getString(movieCursor.getColumnIndexOrThrow(RELEASE_DATE))
            val description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DESCRIPTION))
            listMovie.add(MovieMdl(id, title, poster, release, description))
        }
        return listMovie
    }
}