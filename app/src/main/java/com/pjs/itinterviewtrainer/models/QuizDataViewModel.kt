package com.pjs.itinterviewtrainer.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizDataModel : ViewModel() {
    private val users: MutableLiveData<List<String>> = MutableLiveData(listOf("C++", "Python", "Js"))
    private val levels: MutableLiveData<List<String>> = MutableLiveData(listOf("Easy", "Medium", "Hard"))

    fun getCategories(): LiveData<List<String>> {
        return users
    }

    fun getLevels(): LiveData<List<String>>{
        return levels
    }
}