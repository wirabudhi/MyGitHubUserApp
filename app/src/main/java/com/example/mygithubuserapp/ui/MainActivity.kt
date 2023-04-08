package com.example.mygithubuserapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuserapp.R
import com.example.mygithubuserapp.adapter.UserAdapter
import com.example.mygithubuserapp.databinding.ActivityMainBinding
import com.example.mygithubuserapp.helper.SettingPreferences
import com.example.mygithubuserapp.helper.SettingViewModelFactory
import com.example.mygithubuserapp.viewmodel.SettingViewModel
import com.example.mygithubuserapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val userViewModel by viewModels<UserViewModel>()
    private val settingViewModel by viewModels<SettingViewModel> {
        SettingViewModelFactory(SettingPreferences.getInstance(dataStore))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(listOf())
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        userViewModel.listUser.observe(this) {users ->
            if (users!= null) {
                adapter.updateData(users)
                adapter.notifyDataSetChanged()
            }
        }

        userViewModel.isLoading.observe(this) {isLoading ->
            if (isLoading != null) {
                showLoading(isLoading)
            }
        }

        lifecycleScope.launch {
            userViewModel.getUserData("wira")
        }

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String): Boolean {
                if (q.isEmpty()){
                    return true
                }
                lifecycleScope.launch {
                    userViewModel.getUserData(q)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    lifecycleScope.launch {
                        userViewModel.getUserData("wira")
                    }
                }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> true
            R.id.favorite -> {
                val i = Intent(this, FavoriteUserActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}