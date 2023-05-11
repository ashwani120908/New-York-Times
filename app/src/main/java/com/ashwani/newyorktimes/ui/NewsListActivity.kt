package com.ashwani.newyorktimes.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ashwani.newyorktimes.R
import com.ashwani.newyorktimes.data.NewsData
import com.ashwani.newyorktimes.data.network.Status

import com.ashwani.newyorktimes.ui.adapter.NewsListAdapter
import com.ashwani.newyorktimes.viewmodel.NewsViewModel
import com.ashwani.newyorktimes.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_news_list.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.news_list.*
import javax.inject.Inject

class NewsListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NewsViewModel
    private lateinit var listAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)
        AndroidInjection.inject(this)

        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)

        if (item_detail_container != null) {
            twoPane = true
        }
        initialiseViews()
    }

    private fun initialiseViews() {
        news_list?.apply {
            listAdapter = NewsListAdapter(this@NewsListActivity, twoPane)
            adapter = listAdapter
            setHasFixedSize(true)
        }
        viewModel.apply {
            fetchData(false)
            getNewsLiveData().observe(this@NewsListActivity, Observer {
                when (it.status) {
                    Status.SUCCESS -> displayReposData(it.data)
                    Status.LOADING -> displayLoadingView()
                    Status.ERROR -> displayErrorLayout()
                }
            })
        }
        retry_button.setOnClickListener {
            onRefresh()
        }
    }

    private fun onRefresh() {
        viewModel.fetchData(true)
    }

    private fun displayReposData(news: List<NewsData>?) {
        news?.let {
            listAdapter.setNewsData(news)
            error_layout.visibility = View.GONE
            hideShimmer()
            news_list?.scheduleLayoutAnimation()
        }
    }

    override fun onPause() {
        hideShimmer()
        super.onPause()
    }

    private fun displayLoadingView() {
        if (listAdapter.itemCount == 0) {
            showShimmer()
        }
        error_layout.visibility = View.GONE
    }

    private fun displayErrorLayout() {
        if (listAdapter.itemCount == 0) {
            error_layout.visibility = View.VISIBLE
        } else {
            error_layout.visibility = View.GONE
        }
        hideShimmer()
    }

    private fun showShimmer() {
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
    }

    private fun hideShimmer() {
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.GONE
    }
}
