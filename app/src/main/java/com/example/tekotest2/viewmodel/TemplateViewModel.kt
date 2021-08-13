package com.example.tekotest2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tekotest2.service.repository.TekoRepository
import com.example.tekotest2.model.User
import com.example.tekotest2.utils.NetworkHelper
import com.example.tekotest2.utils.Resource
import kotlinx.coroutines.launch

class TemplateViewModel(private var repository : TekoRepository, private  var networkHelper: NetworkHelper) : ViewModel() {

    val usersLiveData  = MutableLiveData<Resource<List<User>>>()
    fun initData(){
        fetchUsers()
    }


    private fun fetchUsers() {
        viewModelScope.launch {
            usersLiveData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                repository.getUser().let {
                    if (it.isSuccessful) {
                        usersLiveData.postValue(Resource.success(it.body()))
                    } else usersLiveData.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else usersLiveData.postValue(Resource.error("No internet connection", null))
        }
    }
}