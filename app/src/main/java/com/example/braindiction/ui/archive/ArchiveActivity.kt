package com.example.braindiction.ui.archive

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingDataAdapter
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
        PagingPatientsViewModel.PagingViewModelFactory()
    }

    private lateinit var viewModel: ArchiveViewModel
    private lateinit var adapter: PatientAdapter
    private lateinit var adapterPaging: PatientListPagingAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Archive"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = PatientAdapter()
        adapter.notifyDataSetChanged()

        adapterPaging = PatientListPagingAdapter()
        adapterPaging.notifyDataSetChanged()

        binding.apply {
            val layoutManagerPaging = LinearLayoutManager(this@ArchiveActivity)
            rvListArchivePatient.layoutManager = layoutManagerPaging
            rvListArchivePatient.addItemDecoration(
                DividerItemDecoration(
                    this@ArchiveActivity,
                    layoutManagerPaging.orientation
                )
            )
            rvListArchivePatient.setHasFixedSize(true)
            rvListArchivePatient.adapter = adapterPaging
        }

        val loginSession = LoginSession(this)
        val token = loginSession.passToken().toString()
        showLoading(true)
        pagingViewModel.allPatient.observe(this) {
            adapterPaging.submitData(lifecycle, it)
            showLoading(false)
        }

        adapterPaging.setOnItemClickCallback(object : PatientListPagingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PatientData) {
                val intentToDetail = Intent(this@ArchiveActivity, DetailPatientActivity::class.java)
                intentToDetail.putExtra(DetailPatientActivity.EXTRA_NAME, data)
                startActivity(intentToDetail)
            }
        })

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ArchiveViewModel::class.java]

        viewModel.searchPatient.observe(this) {
            if (it != null) {
                adapter.setListPatient(it)
                showLoading(false)
            }
            if (it.isEmpty()) {
                binding.notFoundAnimation.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searchArchive).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_archive)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(patientid: String): Boolean {
                showLoading(true)
                searchView.clearFocus()
                viewModel.setSearchPatient(patientid)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.setOnKeyListener { _, i, keyEvent ->
                    if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
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