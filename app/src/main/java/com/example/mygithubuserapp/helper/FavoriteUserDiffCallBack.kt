package com.example.mygithubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.mygithubuserapp.database.FavoriteUser

class FavoriteUserDiffCallBack(private val oldFavoriteUser: List<FavoriteUser>, private val newFavoriteuser: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFavoriteUser.size
    }
    override fun getNewListSize(): Int {
        return newFavoriteuser.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteUser[oldItemPosition].username == newFavoriteuser[newItemPosition].username
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = oldFavoriteUser[oldItemPosition]
        val newEmployee = newFavoriteuser[newItemPosition]
        return oldEmployee.username == newEmployee.username && oldEmployee.avatarUrl == newEmployee.avatarUrl
    }
}