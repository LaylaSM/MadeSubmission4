package com.laylamac.madesubmission2.view.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.model.MovieMdl

class MovieAdapter(
    private val context: Context,
    val listMovie: MutableList<MovieMdl>,
    private val mListener: OnItemClicked
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val tvTitle: TextView = itemView!!.findViewById(R.id.tv_title_movie)
        val ivPoster: ImageView = itemView!!.findViewById(R.id.iv_poster_movie)
        val tvRelease: TextView = itemView!!.findViewById(R.id.tv_release_date_movie)
        val tvDesc: TextView = itemView!!.findViewById(R.id.tv_desc_movie)
        val cardView: CardView = itemView!!.findViewById(R.id.item_card_view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listMovie[position]
        holder.tvTitle.text = item.title
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w342" + item.poster)
            .into(holder.ivPoster)
        holder.cardView.setOnClickListener {
            mListener.onItemClick(position)
        }
        holder.tvRelease.text = item.release.substringBefore("-")
        holder.tvDesc.text = item.description
    }

    interface OnItemClicked {
        fun onItemClick(position: Int)
    }


}