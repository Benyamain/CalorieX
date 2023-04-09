package com.example.caloriex

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {
    @GET("food-database/v2/parser?app_id=823e82fb&app_key=6e13d81010ab8836ec01368c7b846ca0")
    fun getFoods(@Query("ingr") searchText: String): Single<FoodApiModel>
}