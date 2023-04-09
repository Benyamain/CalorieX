package com.example.caloriex

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FoodApiService {
    private val BASE_URL = "https://api.edamam.com/api/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(FoodApi::class.java)

    fun getData(searchText: String): Single<FoodApiModel> {
        return api.getFoods(searchText)
    }

}