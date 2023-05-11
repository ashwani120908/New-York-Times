package com.ashwani.newyorktimes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ashwani.newyorktimes.MockUtils
import com.ashwani.newyorktimes.data.NewsData
import com.ashwani.newyorktimes.data.network.Resource
import com.ashwani.newyorktimes.data.network.Status
import com.ashwani.newyorktimes.data.repository.NewsRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var dataRepository: NewsRepository

    lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.viewModel = NewsViewModel(this.dataRepository)
    }

    @Test
    fun fetchRepositories_success() {
        val repoLiveData: MutableLiveData<Resource<List<NewsData>>> = MutableLiveData()
        val successResource = Resource(Status.SUCCESS, MockUtils.mockRepositories())
        repoLiveData.postValue(successResource)
        Mockito.`when`(dataRepository.getData()).thenAnswer {
            return@thenAnswer repoLiveData
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<Resource<List<NewsData>?>>
        viewModel.getNewsLiveData().observeForever(observer)
        viewModel.fetchData(ArgumentMatchers.anyBoolean())
        Assert.assertNotNull(viewModel.getNewsLiveData().value)
        Assert.assertEquals(Status.SUCCESS, viewModel.getNewsLiveData().value?.status)
    }

    @Test
    fun fetchRepositories_failure() {
        val repoLiveData: MutableLiveData<Resource<Any>> = MutableLiveData()
        val errorResource = Resource.error<Any>("some error")
        repoLiveData.postValue(errorResource)
        Mockito.`when`(dataRepository.getData()).thenAnswer {
            return@thenAnswer repoLiveData
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<Resource<List<NewsData>?>>
        viewModel.getNewsLiveData().observeForever(observer)
        viewModel.fetchData(ArgumentMatchers.anyBoolean())
        Assert.assertNotNull(viewModel.getNewsLiveData().value)
        Assert.assertEquals(Status.ERROR, viewModel.getNewsLiveData().value?.status)
        Assert.assertEquals("some error", viewModel.getNewsLiveData().value?.errorMessage)
    }

}