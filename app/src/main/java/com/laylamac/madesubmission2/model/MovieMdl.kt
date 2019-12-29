package com.laylamac.madesubmission2.model

import android.database.Cursor
import android.os.Parcelable
import com.laylamac.madesubmission2.db.DatabaseContract
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.DESCRIPTION
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.ID
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.RELEASE_DATE
import com.laylamac.madesubmission2.db.DatabaseContract.MovieColumns.Companion.TITLE
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class MovieMdl(
    var id: String,
    var title: String,
    var poster: String,
    var release: String,
    var description: String
) : Parcelable {

    constructor(`object`: JSONObject) : this(
        `object`.getString("id"),
        `object`.getString("title"),
        `object`.getString("poster_path"),
        `object`.getString("release_date"),
        `object`.getString("overview")

    )

    constructor(cursor: Cursor):this(
        DatabaseContract().getColumnString(cursor, ID),
        DatabaseContract().getColumnString(cursor, TITLE),
        DatabaseContract().getColumnString(cursor, POSTER),
        DatabaseContract().getColumnString(cursor, RELEASE_DATE),
        DatabaseContract().getColumnString(cursor, DESCRIPTION)

        )

}



