package com.example.mygithubuserapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubuserapp.api.ApiConfig
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.repository.FavoriteUserRepository
import com.example.mygithubuserapp.response.DetailUserResponse
import com.example.mygithubuserapp.response.ItemsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse>()

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val repository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun addFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavoriteUser(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavoriteUser(favoriteUser)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return repository.getFavoriteUserByUsername(username)
    }

    suspend fun setDetailUser(username: String) {
        withContext(Dispatchers.IO) {
            ApiConfig.apiService.getDetailUser(username).enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e(this::class.java.simpleName, "On Failure ${t.message}")
                }
            })
        }
    }

    fun getDetailUser(): LiveData<DetailUserResponse> = _detailUser

    suspend fun getFollowers(username: String) {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            ApiConfig.apiService.getFollowers(username).enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.postValue(false)
                        _followers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    Log.e(this::class.java.simpleName, "On Failure ${t.message}")
                    _isLoading.postValue(false)
                }
            })
        }
    }

    suspend fun getFollowing(username: String) {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            ApiConfig.apiService.getFollowing(username).enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.postValue(false)
                        _following.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    Log.e(this::class.java.simpleName, "On Failure ${t.message}")
                    _isLoading.postValue(false)
                }
            })
        }
    }

}