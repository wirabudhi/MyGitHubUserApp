package com.example.mygithubuserapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuserapp.databinding.ItemUserBinding
import com.example.mygithubuserapp.response.ItemsItem
import com.example.mygithubuserapp.ui.DetailActivity

class UserAdapter(private var listUser: List<ItemsItem>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    lateinit var context: Context

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            with(binding) {
                tvName.text = user.login
                Glide.with(context)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(ivProfile)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    fun updateData(newListUser: List<ItemsItem>) {
        val diffResult = DiffUtil.calculateDiff(UsersDiffCallback(newListUser, listUser))
        listUser = newListUser
        diffResult.dispatchUpdatesTo(this)
    }

    private class UsersDiffCallback(
        private val oldListUser: List<ItemsItem>,
        private val newListUser: List<ItemsItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldListUser.size

        override fun getNewListSize(): Int = newListUser.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldListUser[oldItemPosition].login == newListUser[newItemPosition].login

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldListUser[oldItemPosition] == newListUser[newItemPosition]
    }
}