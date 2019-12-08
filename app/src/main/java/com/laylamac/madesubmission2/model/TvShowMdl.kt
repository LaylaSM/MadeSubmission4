package com.laylamac.madesubmission2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject


@Parcelize
data class TvShowMdl(
    var id: String? = null,
    var title: String? = null,
    var poster: String? = null,
    var release: String? = null,
    var description: String? = null
) : Parcelable {

    constructor(`object`: JSONObject) : this(
        `object`.getString("id"),
        `object`.getString("name"),
        `object`.getString("poster_path"),
        `object`.getString("first_air_date"),
        `object`.getString("overview")

    )
}
