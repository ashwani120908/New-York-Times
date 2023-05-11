package com.ashwani.newyorktimes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashwani.newyorktimes.viewmodel.NewsViewModel
import com.ashwani.newyorktimes.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    protected abstract fun bindNewsViewModel(trendingViewModel: NewsViewModel): ViewModel
}