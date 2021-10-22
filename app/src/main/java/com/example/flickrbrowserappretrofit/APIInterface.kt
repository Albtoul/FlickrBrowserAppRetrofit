package com.example.flickrbrowserappretrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("/services/rest/?method=flickr.photos.search&api_key=cb0cbca5c50568f7e3189b08d8e6a89b&tags&per_page=10&format=json&nojsoncallback=1&sort=relevance")
 fun doGetListResources(@Query("text")text:String):Call<PhotoData>
}