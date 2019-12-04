package com.laylamac.madesubmission2.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.DESCRIPTION
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.ID
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.TITLE
import com.laylamac.madesubmission2.db.DatabaseContract.TVShowColumns.Companion.FIRST_AIR_DATE
import com.laylamac.madesubmission2.model.TvShowMdl
import java.sql.SQLException

class TvShowDB(context: Context) {
    private val DATABASE_TABLE = DatabaseContract().TABLE_TV_SHOW
    var mDatabaseHelper = DatabaseHelper(context)
    lateinit var mDatabase: SQLiteDatabase

    companion object {
        @Volatile
        private var INSTANCE: TvShowDB? = null

        fun getInstance(context: Context): TvShowDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TvShowDB(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        mDatabase = mDatabaseHelper.writableDatabase
    }

    fun getDataTvShow(): ArrayList<TvShowMdl> {
        val tvshowList = ArrayList<TvShowMdl>()
        val cursor = mDatabase.query(
            DATABASE_TABLE,
            null, null, null, null, null,
            null, null
        )
        cursor.moveToFirst()
        var tvShow: TvShowMdl
        if (cursor.count > 0) {
            do {
                tvShow = TvShowMdl(
                    cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FIRST_AIR_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                )

                tvshowList.add(tvShow)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return tvshowList
    }

    fun isFavoriteTvShow(id: String): Boolean {
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

    fun insertTvShow(tvShow: TvShowMdl): Long {
        val args = ContentValues()
        args.put(ID, tvShow.id)
        args.put(TITLE, tvShow.title)
        args.put(FIRST_AIR_DATE, tvShow.release)
        args.put(DESCRIPTION, tvShow.description)
        args.put(POSTER, tvShow.poster)
        return mDatabase.insert(DATABASE_TABLE, null, args)
    }

    fun deleteTvShow(id: String): Int {
        return mDatabase.delete(DatabaseContract().TABLE_TV_SHOW, "$ID = '$id'", null)
    }

}