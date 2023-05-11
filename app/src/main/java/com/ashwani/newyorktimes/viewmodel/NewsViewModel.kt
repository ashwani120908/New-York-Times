package com.ashwani.newyorktimes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ashwani.newyorktimes.data.NewsData
import com.ashwani.newyorktimes.data.network.Resource
import com.ashwani.newyorktimes.data.repository.NewsRepository
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val dataRepository: NewsRepository) :
    ViewModel() {

    private val newsListData = MutableLiveData<Boolean>()

    private val newsListLiveData: LiveData<Resource<List<NewsData>?>> =
        Transformations.switchMap(
            newsListData
        ) { input -> dataRepository.getData(input) }

    private val newsDataId = MutableLiveData<Long>()
    private val newsLiveData: LiveData<NewsData> = Transformations.switchMap(
        newsDataId
    ) { input -> dataRepository.getNewsById(input) }

    fun getNewsLiveData() = newsListLiveData

    fun fetchData(forceRefresh: Boolean) {
        newsListData.value = forceRefresh
    }

    fun getNewsData() = newsLiveData

    fun fetchNewsById(id: Long) {
        newsDataId.value = id
    }
}
