package com.project.videocall.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.videocall.R
import com.project.videocall.dto.User

class UserAdapter(private val datas: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val information = datas[position]
        holder.emailText.text = information.email
    }

    override fun getItemCount() = datas.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val emailText: TextView = view.findViewById(R.id.mainEmailTextView)
    }

}