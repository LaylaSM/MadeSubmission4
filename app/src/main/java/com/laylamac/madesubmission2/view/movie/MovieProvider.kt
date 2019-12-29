package com.laylamac.madesubmission2.view.movie

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import com.laylamac.madesubmission2.db.DatabaseContract
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.ID
import com.laylamac.madesubmission2.db.MovieDB
import com.laylamac.madesubmission2.view.favorite.FavoriteMovieFragment

class MovieProvider : ContentProvider() {

    private val MOVIE = 1
    private val MOVIE_ID = 2
    private val mUriMather = UriMatcher(UriMatcher.NO_MATCH)
    lateinit var mMovieDB: MovieDB


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        mMovieDB.open()
        val added: Long = when (mUriMather.match(uri)) {
            MOVIE -> mMovieDB.insertProvider(values as ContentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(
            DatabaseContract.MovieColumns().CONTENT_URI,
            FavoriteMovieFragment.DataObserver(Handler(), context!!))
            return Uri.parse(DatabaseContract.MovieColumns().CONTENT_URI.toString()+"/" + added)
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        mMovieDB.open()
        return when (mUriMather.match(uri)){
            MOVIE -> mMovieDB.queryProvider()
            MOVIE_ID -> mMovieDB.queryByIdProvider(uri.lastPathSegment as String)
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        uriMatching()
        mMovieDB = MovieDB(context!!)
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        mMovieDB.open()
        val update : Int = when (mUriMather.match(uri)){
            MOVIE_ID -> mMovieDB.updateProvider(uri.lastPathSegment as String, values as ContentValues)
            else -> 0
        }
        context!!.contentResolver.notifyChange(DatabaseContract.MovieColumns().CONTENT_URI, FavoriteMovieFragment.DataObserver(Handler(), context!!))
        return update
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
       mMovieDB.open()
        val delete : Int = when (mUriMather.match(uri)){
            MOVIE_ID -> mMovieDB.deleteProvider(uri.lastPathSegment!!)
            else -> 0
        }
        context!!.contentResolver.notifyChange(DatabaseContract.MovieColumns().CONTENT_URI,FavoriteMovieFragment.DataObserver(Handler(), context!!))
        return delete
    }

    override fun getType(uri: Uri): String? {
       return null
    }

    private fun uriMatching() {
        mUriMather.addURI(DatabaseContract().AUTHOR, DatabaseContract().TABLE_MOVIE, MOVIE)
        mUriMather.addURI(
            DatabaseContract().AUTHOR,
            DatabaseContract().TABLE_MOVIE + "/$ID",
            MOVIE_ID
        )

    }
}