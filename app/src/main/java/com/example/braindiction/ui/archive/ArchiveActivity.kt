package com.example.braindiction.ui.archive

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.braindiction.R
import com.example.braindiction.adapter.PatientAdapter
import com.example.braindiction.adapter.PatientListPagingAdapter
import com.example.braindiction.api.PatientData
import com.example.braindiction.databinding.ActivityArchiveBinding
import com.example.braindiction.paginglistpatient.PagingPatientsViewModel
import com.example.braindiction.preference.LoginSession
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.patient.DetailPatientActivity
import com.example.braindiction.viewmodel.ArchiveViewModel

class ArchiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchiveBinding
    private val pagingViewModel: PagingPatientsViewModel by viewModels {
        PagingPatientsViewModel.PagingViewModelFactory(this)
    }

    private lateinit var viewModel: ArchiveViewModel
    private lateinit var adapter: PatientAdapter
    private lateinit var adapterPaging: PatientListPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Archive"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        setupAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupAdapter() {
        adapterPaging = PatientListPagingAdapter()
        adapterPaging.notifyDataSetChanged()

        adapter = PatientAdapter()
        adapter.notifyDataSetChanged()

        adapterPaging.setOnItemClickCallback(object : PatientListPagingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PatientData) {
                val intentToDetail = Intent(this@ArchiveActivity, DetailPatientActivity::class.java)
                intentToDetail.putExtra(DetailPatientActivity.EXTRA_NAME, data)
                startActivity(intentToDetail)
            }
        })

        adapter.setOnItemClickCallback(object : PatientAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PatientData) {
                val intentToDetail = Intent(this@ArchiveActivity, DetailPatientActivity::class.java)
                intentToDetail.putExtra(DetailPatientActivity.EXTRA_NAME, data)
                startActivity(intentToDetail)
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ArchiveViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val loginSession = LoginSession(this)

        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searchArchive).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_archive)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(patientid: String): Boolean {
                showLoading(true)
                searchView.clearFocus()
                viewModel.setSearchPatient("Bearer ${loginSession.passToken().toString()}",patientid)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.setOnKeyListener { _, i, keyEvent ->
                    if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
                }
                if (newText.isNotEmpty()) {
                    val layoutManager = LinearLayoutManager(this@ArchiveActivity)
                    binding.rvListArchivePatient.layoutManager = layoutManager
                    binding.rvListArchivePatient.addItemDecoration(
                        DividerItemDecoration(
                            this@ArchiveActivity,
                            layoutManager.orientation
                        )
                    )
                    binding.rvListArchivePatient.setHasFixedSize(true)
                    binding.rvListArchivePatient.adapter = adapter

                    viewModel.searchPatient.observe(this@ArchiveActivity) {
                        showLoading(true)
                        if (it != null) {
                            adapter.setListPatient(it)
                            showLoading(false)
                        }
                        if (it.isEmpty()) {
                            binding.notFoundAnimation.visibility = View.VISIBLE
                        }
                    }
                }
                if (newText.isEmpty()) {
                    val layoutManagerPaging = LinearLayoutManager(this@ArchiveActivity)
                    binding.rvListArchivePatient.layoutManager = layoutManagerPaging
                    binding.rvListArchivePatient.addItemDecoration(
                        DividerItemDecoration(
                            this@ArchiveActivity,
                            layoutManagerPaging.orientation
                        )
                    )
                    binding.rvListArchivePatient.setHasFixedSize(true)
                    binding.rvListArchivePatient.adapter = adapterPaging

                    showLoading(true)
                    pagingViewModel.allPatient.observe(this@ArchiveActivity) {
                        adapterPaging.submitData(lifecycle, it)
                        showLoading(false)
                    }
                }
                return false

            }
        })

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, HomeActivity::class.java)
        startActivity(backTo)
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}