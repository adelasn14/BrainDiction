package com.example.braindiction.ui.prediction

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.braindiction.R
import com.example.braindiction.adapter.HistoryPredictionAdapter
import com.example.braindiction.api.User
import com.example.braindiction.databinding.FragmentHistoryPredictionBinding
import com.example.braindiction.preference.LoginSession
import com.example.braindiction.ui.patient.DetailPatientActivity
import com.example.braindiction.viewmodel.ArchiveViewModel

class HistoryPredictionFragment(context: Context) : Fragment(R.layout.fragment_history_prediction) {

    private var _binding: FragmentHistoryPredictionBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ArchiveViewModel
    private lateinit var adapter: HistoryPredictionAdapter
    private var patientid: Int? = null

    private var loginSession: LoginSession = LoginSession(context)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        patientid = args?.getInt(DetailPatientActivity.EXTRA_ID)
        _binding = FragmentHistoryPredictionBinding.bind(view)

        adapter = HistoryPredictionAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : HistoryPredictionAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(activity, DetailPatientActivity::class.java)
                intentToDetail.putExtra(DetailPatientActivity.EXTRA_NAME, data.prediction)
                startActivity(intentToDetail)
            }
        })

        binding?.apply {
            rvHistoryPrediction.layoutManager = LinearLayoutManager(activity)
            rvHistoryPrediction.setHasFixedSize(true)
            rvHistoryPrediction.adapter = adapter
        }

        viewModel =
            ViewModelProvider(this)[ArchiveViewModel::class.java]

        val token = loginSession.passToken().toString()
        viewModel.setHistoryPrediction("Bearer : $token", patientid!!)
        viewModel.listHistoryPrediction.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setListPrediction(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}