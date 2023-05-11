package com.ashwani.newyorktimes.di

import com.ashwani.newyorktimes.ui.NewsDetailActivity
import com.ashwani.newyorktimes.ui.NewsDetailFragment
import com.ashwani.newyorktimes.ui.NewsListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeNewsListActivity(): NewsListActivity

    @ContributesAndroidInjector
    internal abstract fun contributeNewsDetailActivity(): NewsDetailActivity

    @ContributesAndroidInjector
    internal abstract fun contributeNewsDetailFragment(): NewsDetailFragment
}