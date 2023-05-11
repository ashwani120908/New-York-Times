package com.ashwani.newyorktimes.data

import com.ashwani.newyorktimes.MockUtils
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class TrendingApiTest : BaseApiTest<DataApi>() {

    private lateinit var dataApi: DataApi

    @Before
    fun init() {
        dataApi = createService(DataApi::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun getRepository() {
        enqueueResponse("response.json")
        val newsSource = MockUtils.getValue(dataApi.fetchNews()).data
        mockWebServer.takeRequest()
        Assert.assertThat(newsSource, CoreMatchers.notNullValue())
        val news1 = newsSource?.results?.get(0)
        Assert.assertThat(news1, CoreMatchers.notNullValue())
        if (news1 != null) {
            Assert.assertEquals("This Tom Hanks Story Will Help You Feel Less Bad", news1.title)
            Assert.assertEquals("The New York Times", news1.source)
            Assert.assertEquals("2019-11-13", news1.published_date)
            Assert.assertEquals("https://www.nytimes.com/2019/11/13/movies/tom-hanks-mister-rogers.html", news1.url)
        }
    }
}