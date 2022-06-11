package com.example.braindiction.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.braindiction.api.PatientData
import com.example.braindiction.databinding.ListPatientBinding

class PatientAdapter : RecyclerView.Adapter<PatientAdapter.SearchViewHolder>() {

    private val searchPatientData = ArrayList<PatientData>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListPatient(users: ArrayList<PatientData>) {
        searchPatientData.clear()
        searchPatientData.addAll(users)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val (patientid, name, _, _) = searchPatientData[position]
        holder.binding.tvPatient.text = StringBuilder().append("ID : ").append(patientid)
        holder.binding.tvNorm.text = name.toString()

        holder.binding.tvPatient.setOnClickListener {
            onItemClickCallback.onItemClicked(searchPatientData[holder.adapterPosition])
        }

    }

    override fun getItemCount() = searchPatientData.size

    class SearchViewHolder(var binding: ListPatientBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: PatientData)
    }
}
