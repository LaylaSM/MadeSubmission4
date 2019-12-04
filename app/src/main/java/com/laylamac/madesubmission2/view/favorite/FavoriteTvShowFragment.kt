package com.laylamac.madesubmission2.view.favorite


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.db.TvShowDB
import com.laylamac.madesubmission2.model.TvShowMdl
import com.laylamac.madesubmission2.view.detail.DetailActivity
import com.laylamac.madesubmission2.view.tvshow.TvShowAdapter
import kotlinx.android.synthetic.main.fragment_favorite_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvShowFragment : Fragment() {

    lateinit var mTvShowDB: TvShowDB
    lateinit var mFavoriteTvShowList: ArrayList<TvShowMdl>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTvShowDB = TvShowDB.getInstance(context!!)
        mTvShowDB.open()

        mFavoriteTvShowList = mTvShowDB.getDataTvShow()

        favorite_tv_show_recycler.apply {
            adapter =
                TvShowAdapter(context, mFavoriteTvShowList, object : TvShowAdapter.OnItemClicked {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("extra_type", "tv_show_favorite")
                        intent.putExtra("fav_tv_show", mFavoriteTvShowList[position])
                        startActivity(intent)
                    }
                })
            val llm = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            llm.stackFromEnd = true
            layoutManager = llm
        }

        favorite_tv_show_progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        mFavoriteTvShowList.clear()
        mFavoriteTvShowList.addAll(mTvShowDB.getDataTvShow())
        favorite_tv_show_recycler.adapter?.notifyDataSetChanged()
    }

}
