package com.example.mygithubuserapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.databinding.ItemUserBinding
import com.example.mygithubuserapp.helper.FavoriteUserDiffCallBack


class FavoriteUserAdapter(private val listFavoriteUser: ArrayList<FavoriteUser>) : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {
    lateinit var context: Context

    fun setListFavoriteUser(listFavoriteUser: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallBack(this.listFavoriteUser, listFavoriteUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUser.clear()
        this.listFavoriteUser.addAll(listFavoriteUser)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
    }
    override fun getItemCount(): Int {
        return listFavoriteUser.size
    }
    inner class FavoriteUserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listFavoriteUser: FavoriteUser) {
            with(binding) {
                tvName.text = listFavoriteUser.username
                Glide.with(context)
                    .load(listFavoriteUser.avatarUrl)
                    .into(ivProfile)
            }
        }
    }
}