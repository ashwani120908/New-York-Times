package com.ashwani.newyorktimes.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ashwani.newyorktimes.R
import com.ashwani.newyorktimes.databinding.NewsDetailBinding
import com.ashwani.newyorktimes.viewmodel.NewsViewModel
import com.ashwani.newyorktimes.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.news_detail.view.*
import javax.inject.Inject

class NewsDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: NewsDetailBinding
    private lateinit var viewModel: NewsViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.news_detail, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)
        arguments?.let {
            if (it.containsKey(KEY_NEWS_ID)) {
                viewModel.fetchNewsById(it.getLong(KEY_NEWS_ID))
                viewModel.getNewsData().observe(viewLifecycleOwner, Observer {news ->
                    activity?.header?.let {header ->
                        if(!news?.media.isNullOrEmpty()) {
                            Glide.with(this@NewsDetailFragment).load(news?.media?.get(0)!!.mediaList?.get(0)?.url).into(header)
                        }
                    }
                    binding.itemDetail.text = news?.title
                })
            }
        }
    }

    companion object {
        const val KEY_NEWS_ID = "news_id"
    }
}
