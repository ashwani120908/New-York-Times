package com.ashwani.newyorktimes.data

import androidx.lifecycle.LiveData
import com.ashwani.newyorktimes.data.network.Resource
import com.ashwani.newyorktimes.di.ApiModule
import retrofit2.http.GET
import retrofit2.http.Query

interface DataApi {

    @GET("mostpopular/v2/viewed/7.json")
    fun fetchNews(@Query("api-key") apiKey : String = ApiModule.API_KEY): LiveData<Resource<NewsSource>>
}