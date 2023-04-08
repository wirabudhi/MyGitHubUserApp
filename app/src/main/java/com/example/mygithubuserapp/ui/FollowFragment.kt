package com.example.mygithubuserapp.ui

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuserapp.adapter.FollowerFollowingAdapter
import com.example.mygithubuserapp.databinding.FragmentFollowBinding
import com.example.mygithubuserapp.helper.ViewModelFactory
import com.example.mygithubuserapp.viewmodel.DetailUserViewModel
import kotlinx.coroutines.launch

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>(){
        ViewModelFactory.getInstance(application = Application())
    }
    private lateinit var adapter: FollowerFollowingAdapter
    private var position = 0
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowerFollowingAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.adapter = adapter

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        lifecycleScope.launch {
            if (position == 1) {
                detailUserViewModel.getFollowers(username)
                detailUserViewModel.followers.observe(viewLifecycleOwner) { followers ->
                    adapter.submitList(followers)
                }
            } else {
                detailUserViewModel.getFollowing(username)
                detailUserViewModel.following.observe(viewLifecycleOwner) { following ->
                    adapter.submitList(following)
                }
            }
        }

        detailUserViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}