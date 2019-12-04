package com.laylamac.madesubmission2.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.DESCRIPTION
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.ID
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.RELEASE_DATE
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.TITLE
import com.laylamac.madesubmission2.model.MovieMdl
import java.sql.SQLException

class MovieDB(context: Context) {
    val DATABASE_TABLE = DatabaseContract().TABLE_MOVIE
    var mDatabaseHelper = DatabaseHelper(context)
    lateinit var mDatabase: SQLiteDatabase

    companion object {
        @Volatile
        private var INSTANCE: MovieDB? = null

        fun getInstance(context: Context): MovieDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieDB(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        mDatabase = mDatabaseHelper.writableDatabase
    }

    fun getDataMovies(): ArrayList<MovieMdl> {
        val movieList = ArrayList<MovieMdl>()
        val cursor = mDatabase.query(
            DATABASE_TABLE,
            null, null, null, null, null,
            null, null
        )
        cursor.moveToFirst()
        var movie: MovieMdl
        if (cursor.count > 0) {
            do {
                movie = MovieMdl(
                    cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                )

                movieList.add(movie)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return movieList
    }

    fun isFavoriteMovie(id: String): Boolean {
        val cursor = mDatabase.query(
            DATABASE_TABLE,
            null, "$ID = '$id'", null, null, null,
            null, null
        )
        cursor.moveToFirst()
        if (cursor.count > 0) {
            return true
        }
        return false

    }

    fun insertMovie(movie: MovieMdl): Long {
        val args = ContentValues()
        Log.d("COBA ", "ID = " + movie.title)
        args.put(ID, movie.id)
        args.put(TITLE, movie.title)
        args.put(POSTER, movie.poster)
        args.put(RELEASE_DATE, movie.release)
        args.put(DESCRIPTION, movie.description)
        return mDatabase.insert(DATABASE_TABLE, null, args)
    }

    fun deleteMovie(id: String): Int {
        return mDatabase.delete(DatabaseContract().TABLE_MOVIE, "$ID = '$id'", null)
    }


}
