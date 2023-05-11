package com.ashwani.newyorktimes.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ashwani.newyorktimes.BR
import com.ashwani.newyorktimes.R
import com.ashwani.newyorktimes.data.NewsData
import com.ashwani.newyorktimes.databinding.NewsListContentBinding
import com.ashwani.newyorktimes.ui.NewsDetailActivity
import com.ashwani.newyorktimes.ui.NewsDetailFragment
import com.ashwani.newyorktimes.ui.NewsListActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class NewsListAdapter(
    private val parentActivity: NewsListActivity,
    private val twoPane: Boolean
) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as NewsData
        if (twoPane) {
            val fragment = NewsDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(NewsDetailFragment.KEY_NEWS_ID, item.id)
                }
            }
            parentActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(v.context, NewsDetailActivity::class.java).apply {
                putExtra(NewsDetailFragment.KEY_NEWS_ID, item.id)
            }
            v.context.startActivity(intent)
        }
    }
    private val newsList: MutableList<NewsData> = mutableListOf()

    fun setNewsData(news: List<NewsData>?) {
        newsList.clear()
        news?.let {
            newsList.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : NewsListContentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.news_list_content, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsList[position]
        holder.bind(item)
        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
        if(!item.media.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(item.media?.get(0)!!.mediaList?.get(0)?.url)
                .into(holder.newsIcon)
        }
    }

    override fun getItemCount() = newsList.size

    inner class ViewHolder(val binding: NewsListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val newsIcon : CircleImageView = binding.newsIcon

        fun bind(data : NewsData) {
            binding.setVariable(BR.newsData, data)
            binding.executePendingBindings()
        }
    }
}