package com.example.mygithubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.database.FavoriteUserDao
import com.example.mygithubuserapp.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val favoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUsers()

    fun addFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute{favoriteUserDao.insert(favoriteUser)}
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute{favoriteUserDao.delete(favoriteUser)}
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = favoriteUserDao.getFavoriteUserByUsername(username)
}
