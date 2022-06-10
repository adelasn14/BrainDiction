package com.example.braindiction.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.braindiction.api.PatientData
import com.example.braindiction.api.User
import com.example.braindiction.databinding.ListPatientBinding
import com.example.braindiction.databinding.ListPredictionBinding

class HistoryPredictionAdapter : RecyclerView.Adapter<HistoryPredictionAdapter.SearchViewHolder>() {

    private val searchPredictionHistory = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListPrediction(users: ArrayList<User>) {
        searchPredictionHistory.clear()
        searchPredictionHistory.addAll(users)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListPredictionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val (prediction, history) = searchPredictionHistory[position]
        holder.binding.tvPrediction.text = StringBuilder().append("History : ").append(prediction)
        holder.binding.tvHistory.text = history

    }

    override fun getItemCount() = searchPredictionHistory.size

    class SearchViewHolder(var binding: ListPredictionBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}
