package com.laam.dogandroidjetpacknew.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}