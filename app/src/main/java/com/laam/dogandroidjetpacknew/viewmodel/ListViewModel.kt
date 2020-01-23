package com.laam.dogandroidjetpacknew.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laam.dogandroidjetpacknew.model.DogApiService
import com.laam.dogandroidjetpacknew.model.DogBreed
import com.laam.dogandroidjetpacknew.model.DogDatabase
import com.laam.dogandroidjetpacknew.util.NotificationHelper
import com.laam.dogandroidjetpacknew.util.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {
    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private var prefHelper = SharedPreferenceHelper(getApplication())

    private val serviceApi = DogApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshWithByPassCache() {
        fetchFromRemote()
    }

    fun refresh() {
        val updateTime = SharedPreferenceHelper().getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            Log.d("laam", "doglist : $dogs")
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dog retrieved from database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true

        disposable.add(
            serviceApi.getDog()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(t: List<DogBreed>) {
                        Toast.makeText(
                            getApplication(),
                            "Dog retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                        storeDogsLocally(t)
                        NotificationHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        dogLoadError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dogsRetrieved(list: List<DogBreed>) {
        loading.value = false
        dogLoadError.value = false
        dogs.value = list
    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            for (i in list.indices) {
                list[i].uuid = result[i].toInt()
            }
            dogsRetrieved(list)
        }

        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}