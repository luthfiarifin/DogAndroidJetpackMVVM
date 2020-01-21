package com.laam.dogandroidjetpacknew.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.laam.dogandroidjetpacknew.model.DogBreed
import com.laam.dogandroidjetpacknew.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dog = MutableLiveData<DogBreed>()

    fun fetch(dogUuid: Int) {
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getDog(dogUuid)
            Log.d("laam", "dog : $dogs")
            dog.value = dogs[0]
        }
    }
}