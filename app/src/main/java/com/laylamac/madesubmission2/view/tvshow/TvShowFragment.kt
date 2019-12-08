package com.laylamac.madesubmission2.view.tvshow


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.model.TvShowMdl
import com.laylamac.madesubmission2.view.detail.DetailActivity
import com.laylamac.madesubmission2.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var mAdapter: TvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.tvShow.observe(this, setDataTv)
        movieViewModel.setMoviesTvShows("tv")

        pb_tvShow_fragment.visibility = View.VISIBLE
    }

    private val setDataTv = Observer<ArrayList<TvShowMdl>> { tvShow ->
        if (tvShow != null) {
            mAdapter = TvShowAdapter(context!!, tvShow, object : TvShowAdapter.OnItemClicked {
                override fun onItemClick(position: Int) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("extra_tv_show_id", tvShow[position].id)
                    intent.putExtra("extra_type", "tv")
                    startActivity(intent)
                }

            })

            tv_show_rv.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            pb_tvShow_fragment.visibility = View.GONE
        }
    }
}

