package com.example.mygithubuserapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuserapp.adapter.FavoriteUserAdapter
import com.example.mygithubuserapp.adapter.UserAdapter
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.databinding.ActivityFavoriteUserBinding
import com.example.mygithubuserapp.helper.ViewModelFactory
import com.example.mygithubuserapp.response.ItemsItem
import com.example.mygithubuserapp.viewmodel.FavoriteUserViewModel

class FavoriteUserActivity : AppCompatActivity() {
    private val favoriteUserViewModel by viewModels<FavoriteUserViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var _activityFavoriteUser: ActivityFavoriteUserBinding? = null
    private val binding get() = _activityFavoriteUser

    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteUser = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        favoriteUserViewModel.getAllFavoriteUsers().observe(this) { users: List<FavoriteUser> ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = it.avatarUrl?.let { it1 -> ItemsItem(login = it.username, avatarUrl = it1) }
                if (item != null) {
                    items.add(item)
                }
            }
            binding?.rvUser?.adapter = UserAdapter(items)
        }

        adapter = FavoriteUserAdapter(ArrayList())

        binding?.rvUser?.layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.setHasFixedSize(true)
        binding?.rvUser?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteUser = null
    }
}