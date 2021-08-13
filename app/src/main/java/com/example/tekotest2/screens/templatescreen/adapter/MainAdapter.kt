package com.example.tekotest2.screens.templatescreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tekotest2.R
import com.example.tekotest2.databinding.ItemLayoutBinding
import com.example.tekotest2.model.User

class MainAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            var binding : ItemLayoutBinding? = DataBindingUtil.bind(itemView)
            binding?.textViewUserName?.text = user.name
            binding?.textViewUserEmail?.text = user.email
            binding?.imageViewAvatar?.let {
                Glide.with(binding?.imageViewAvatar.context)
                    .load(user.avatar)
                    .into(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position])

    fun addData(list: List<User>) {
        users.addAll(list)
    }
}