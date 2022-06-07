package com.example.braindiction.ui.archive

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.braindiction.R
import com.example.braindiction.adapter.PatientAdapter
import com.example.braindiction.databinding.ActivityArchiveBinding
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.viewmodel.ArchiveViewModel

class ArchiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchiveBinding
    private lateinit var viewModel: ArchiveViewModel
    private lateinit var adapter: PatientAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Archive"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = PatientAdapter()
        adapter.notifyDataSetChanged()

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

        viewModel.displayAllPatient()

        binding.apply {
            val layoutManager = LinearLayoutManager(this@ArchiveActivity)
            rvListArchivePatient.layoutManager = layoutManager
            rvListArchivePatient.addItemDecoration(
                DividerItemDecoration(
                    this@ArchiveActivity,
                    layoutManager.orientation
                )
            )
            rvListArchivePatient.setHasFixedSize(true)
            rvListArchivePatient.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searchArchive).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_archive)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                searchView.clearFocus()
                viewModel.setSearchPatient(query)
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