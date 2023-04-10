package com.example.caloriex

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val _foodApiService = FoodApiService()
    private val _disposable = CompositeDisposable()

    val foodsData = MutableLiveData<List<FoodApiModel>>()
    val foodsError = MutableLiveData<String>()

    fun getData(searchText: String) {
        getDataFromAPI(searchText)
    }

    private fun getDataFromAPI(searchText: String) {
        val liveData = MutableLiveData<String>()
        _disposable.add(
            _foodApiService.getData(searchText)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<FoodApiModel>() {

                    override fun onSuccess(t: FoodApiModel) {
                        foodsData.value = listOf(t)
                        Log.d("foodsData", "$t")
                        foodsError.value = ""
                        liveData.postValue("Success")
                    }

                    override fun onError(e: Throwable) {
                        foodsError.value = e.message
                    }
                })
        )
    }
}