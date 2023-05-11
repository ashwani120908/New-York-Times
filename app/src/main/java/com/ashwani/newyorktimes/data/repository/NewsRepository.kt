package com.ashwani.newyorktimes.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ashwani.newyorktimes.data.DataApi
import com.ashwani.newyorktimes.data.NewsData
import com.ashwani.newyorktimes.data.NewsSource
import com.ashwani.newyorktimes.data.network.Resource
import com.ashwani.newyorktimes.db.NewsDao
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val dataDao: NewsDao,
    private val dataApi: DataApi,
    private val appRequestExecutors: AppRequestExecutors = AppRequestExecutors()
) {

    companion object {
        val TIMEOUT = TimeUnit.HOURS.toMillis(2)
    }

    fun getData(forceRefresh: Boolean = false): MutableLiveData<Resource<List<NewsData>?>> =
        object : NetworkBoundResource<List<NewsData>, NewsSource>(appRequestExecutors) {
            override fun saveCallResult(item: NewsSource) {
                dataDao.deleteAndInsertWithTimeStamp(item.results)
            }

            override fun shouldFetch(data: List<NewsData>?): Boolean =
                data == null || data.isEmpty() || isCacheTimedOut(data) || forceRefresh

            override fun loadFromDb(): LiveData<List<NewsData>> = dataDao.getNews()

            override fun createCall(): LiveData<Resource<NewsSource>> =
                dataApi.fetchNews()

        }.asLiveData()

    fun getNewsById(id : Long) : LiveData<NewsData> = dataDao.getNewsById(id)

    private fun isCacheTimedOut(data: List<NewsData>?): Boolean {
        data?.let {
            val lastFetched = it[0].createdAt
            val now = System.currentTimeMillis()
            if ((now - lastFetched) > TIMEOUT) {
                return true
            }
        }
        return false
    }
}