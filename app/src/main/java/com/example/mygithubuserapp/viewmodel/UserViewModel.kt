package com.example.mygithubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuserapp.api.ApiConfig
import com.example.mygithubuserapp.response.ItemsItem
import com.example.mygithubuserapp.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getUserData(q: String) {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            ApiConfig.apiService.getUser(q).enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.postValue(false)
                    _listUser.postValue(response.body()?.items)
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                }
            })
        }
    }
}