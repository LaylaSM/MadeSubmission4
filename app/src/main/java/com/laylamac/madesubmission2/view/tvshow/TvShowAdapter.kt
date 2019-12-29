package com.laylamac.madesubmission2.view.tvshow

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
import com.laylamac.madesubmission2.model.TvShowMdl

class TvShowAdapter(
    private val context: Context,
    val listTv: MutableList<TvShowMdl>,
    private val mListener: OnItemClicked
) : RecyclerView.Adapter<TvShowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_movie, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val tvTitle: TextView = itemView!!.findViewById(R.id.tv_title_movie)
        val ivPoster: ImageView = itemView!!.findViewById(R.id.iv_poster_movie)
        val tvRelease: TextView = itemView!!.findViewById(R.id.tv_release_date_movie)
        val tvDesc: TextView = itemView!!.findViewById(R.id.tv_desc_movie)
        val cardView: CardView = itemView!!.findViewById(R.id.item_card_view)

    }

    override fun getItemCount(): Int {
        return listTv.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTv[position]
        holder.tvTitle.text = item.title
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w342" + item.poster)
            .into(holder.ivPoster)
        holder.cardView.setOnClickListener {
            mListener.onItemClick(position)
        }
        holder.tvRelease.text = item.release!!.substringBefore("-")
        holder.tvDesc.text = item.description
    }

    interface OnItemClicked {
        fun onItemClick(position: Int)
    }

}
