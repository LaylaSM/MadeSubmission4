package com.laylamac.madesubmission2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATA_VERSION) {

    companion object {
        var DATABASE_NAME = "madesubmission2"
        private val DATA_VERSION = 1
    }

    private val SQL_CREATE_TABLE_MOVIE = String.format(
        "CREATE TABLE %s"
                + " (%s TEXT PRIMARY KEY NOT NULL," +
                " %s TEXT NOT NULL," +
                " %s TEXT NOT NULL," +
                " %s TEXT NOT NULL," +
                " %s TEXT NOT NULL)",
        DatabaseContract().TABLE_MOVIE,
        DatabaseContract.MovieColumns.ID,
        DatabaseContract.MovieColumns.TITLE,
        DatabaseContract.MovieColumns.POSTER,
        DatabaseContract.MovieColumns.RELEASE_DATE,
        DatabaseContract.MovieColumns.DESCRIPTION
    )

    private val SQL_CREATE_TABLE_TV_SHOW = String.format(
        "CREATE TABLE %s"
                + " (%s TEXT PRIMARY KEY NOT NULL," +
                " %s TEXT NOT NULL," +
                " %s TEXT NOT NULL," +
                " %s TEXT NOT NULL," +
                " %s TEXT NOT NULL)",
        DatabaseContract().TABLE_TV_SHOW,
        DatabaseContract.TVShowColumns.ID,
        DatabaseContract.TVShowColumns.TITLE,
        DatabaseContract.TVShowColumns.POSTER,
        DatabaseContract.TVShowColumns.FIRST_AIR_DATE,
        DatabaseContract.TVShowColumns.DESCRIPTION
    )


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
        db?.execSQL(SQL_CREATE_TABLE_TV_SHOW)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + DatabaseContract().TABLE_MOVIE)
        db?.execSQL("DROP TABLE IF EXISTS " + DatabaseContract().TABLE_TV_SHOW)

    }
}