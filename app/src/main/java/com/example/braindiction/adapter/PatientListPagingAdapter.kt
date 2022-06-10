package com.example.braindiction.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.braindiction.api.PatientData
import com.example.braindiction.databinding.ListPatientBinding
import com.example.braindiction.ui.patient.DetailPatientActivity

class PatientListPagingAdapter :
    PagingDataAdapter<PatientData, PatientListPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ListPatientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PatientData) {
            binding.apply {
                tvPatient.text = data.name
                tvNorm.text = data.patientid.toString()

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailPatientActivity::class.java)
                    intent.putExtra(DetailPatientActivity.EXTRA_NAME, data)
                    Log.d("data : ", data.toString())

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PatientData)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PatientData>() {
            override fun areItemsTheSame(oldItem: PatientData, newItem: PatientData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PatientData, newItem: PatientData): Boolean {
                return oldItem.patientid == newItem.patientid
            }
        }
    }
}
