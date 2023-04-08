package com.example.mygithubuserapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubuserapp.R
import com.example.mygithubuserapp.adapter.SectionPagerAdapter
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.databinding.ActivityDetailBinding
import com.example.mygithubuserapp.helper.ViewModelFactory
import com.example.mygithubuserapp.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailUserViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username!= null) {
            showLoading(true)
            lifecycleScope.launch {
                detailViewModel.setDetailUser(username)
            }
            detailViewModel.getDetailUser().observe(this) {detailUserResponse ->
                with(binding) {
                    tvName.text = detailUserResponse.name
                    tvUsername.text = detailUserResponse.login
                    tvFollowers.text = resources.getString(R.string.followers_count, detailUserResponse.followers)
                    tvFollowing.text = resources.getString(R.string.following_count, detailUserResponse.following)
                    Glide.with(this@DetailActivity)
                        .load(detailUserResponse.avatarUrl)
                        .circleCrop()
                        .into(ivProfile)
                    showLoading(false)
                }
            }
        }

        setUpViewPager(username)

        if (username != null) {
            detailViewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
                if (favoriteUser == null) {
                    binding.fab.setImageResource(R.drawable.baseline_favorite_border_24)
                    binding.fab.setOnClickListener {
                        val favUser = FavoriteUser(
                            username = username,
                            avatarUrl = detailViewModel.getDetailUser().value?.avatarUrl ?: "",
                        )
                        detailViewModel.addFavoriteUser(favUser)
                        Toast.makeText(this, "Added to favorite users", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.fab.setImageResource(R.drawable.baseline_favorite_24)
                    binding.fab.setOnClickListener {
                        detailViewModel.deleteFavoriteUser(favoriteUser)
                        Toast.makeText(this, "Removed to favorite users", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUpViewPager(username: String?) {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Followers"
            } else {
                tab.text = "Following"
            }
        }.attach()
    }
}