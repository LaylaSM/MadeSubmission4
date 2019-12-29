package com.laylamac.madesubmission2.utils

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.db.MovieDB
import com.bumptech.glide.request.target.Target
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.view.favorite.FavoriteWidget

class StackRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mItemWidgets = ArrayList<MovieMdl>()
    lateinit var mMovieDB: MovieDB

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        mMovieDB = MovieDB.getInstance(context)
        mMovieDB.open()
        mItemWidgets.clear()
        mItemWidgets.addAll(mMovieDB.getDataMovies())
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean {
       return false
    }

    @Suppress("DEPRECATION")
    override fun getViewAt(position: Int): RemoteViews {
       val remoteView = RemoteViews(context.packageName, R.layout.item_widget)
        val poster = Glide.with(context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w342" + mItemWidgets[position].poster)
            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get()

        remoteView.setImageViewBitmap(R.id.img_widget, poster)
        val itemExtra = Bundle()
        itemExtra.putInt(FavoriteWidget().EXTRA_ITEM, position)
        val fillIntent = Intent()
        fillIntent.putExtras(itemExtra)
        remoteView.setOnClickFillInIntent(R.id.img_widget, fillIntent)
        return remoteView
    }

    override fun getCount(): Int {
       return mItemWidgets.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

}
